package com.catcher.batch.infrastructure.adapter;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.FestivalApiResponse;
import com.catcher.batch.core.service.ApiService;
import com.catcher.batch.core.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Qualifier("festival")
public class FestivalAdapter implements ApiService<Void> {
    private static final int REQUEST_COUNT = 100;
    private final CatcherFeignService catcherFeignService;
    private final FestivalService festivalService;

    @Override
    public <T> T getData() {
        Map<String, Object> param = createParam(1, 1);
        FestivalApiResponse response = catcherFeignService.parseService(param, FestivalApiResponse.class);

        int totalCount = response.getTotalCount().intValue();
        int totalPage = calculatePage(totalCount);

        List<FestivalApiResponse.FestivalItem> festivalList = new ArrayList<>(totalCount);

        IntStream.rangeClosed(1, totalPage)
                .parallel()
                .forEach(page -> {
                    FestivalApiResponse festivalResponse = catcherFeignService.parseService(createParam(page, REQUEST_COUNT), FestivalApiResponse.class);
                    festivalList.addAll(festivalResponse.getItems());
                });

        festivalService.batch(festivalList);

        return null;
    }

    private int calculatePage(int totalCount) {
        if (totalCount % 100 == 0) {
            return totalCount / REQUEST_COUNT;
        }
        return (totalCount / REQUEST_COUNT) + 1;
    }

    private Map<String, Object> createParam(int pageNo, int count) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("numOfRows", count);
        params.put("type", "json");

        return params;
    }
}
