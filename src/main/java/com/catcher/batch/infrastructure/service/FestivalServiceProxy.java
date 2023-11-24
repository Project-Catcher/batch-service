package com.catcher.batch.infrastructure.service;

import com.catcher.batch.common.service.CatcherJsonService;
import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.dto.FestivalApiResponse;
import com.catcher.batch.core.port.AddressPort;
import com.catcher.batch.core.service.FestivalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FestivalServiceProxy extends FestivalService {
    private int requestCount = 100;
    private final CatcherJsonService catcherJsonService;

    public FestivalServiceProxy(CatcherItemRepository catcherItemRepository,
                                CategoryRepository categoryRepository,
                                LocationRepository locationRepository,
                                CatcherJsonService catcherJsonService,
                                AddressPort addressPort) {
        super(catcherItemRepository, categoryRepository, locationRepository, addressPort);
        this.catcherJsonService = catcherJsonService;
    }

    public int getRefreshTotal() {
        Map<String, Object> param = getParam(1, 1);
        FestivalApiResponse block = catcherJsonService.parseService(param, FestivalApiResponse.class).block();

        return block.getTotalCount();
    }

    public void handleBatch() {
        int totalCount = getRefreshTotal();
        int page = 1;
        int len = totalCount % 100 == 0 ? (totalCount / 100) + 1 : (totalCount / 100);

        for (int i = page; i <= len; i++) {
            Map<String, Object> param = getParam(i, requestCount);

            FestivalApiResponse festivalApiResponse = catcherJsonService.parseService(param, FestivalApiResponse.class)
                    .block();

            batch(festivalApiResponse.getItems());
        }
    }

    private Map<String, Object> getParam(int pageNo, int count) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("numOfRows", count);
        params.put("type", "json");

        return params;
    }
}
