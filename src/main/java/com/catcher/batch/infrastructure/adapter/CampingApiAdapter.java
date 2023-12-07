package com.catcher.batch.infrastructure.adapter;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.core.service.ApiService;
import com.catcher.batch.core.service.CampingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Qualifier("camping")
public class CampingApiAdapter implements ApiService<Void> {
    private final CatcherFeignService catcherFeignService;
    private final CampingService campingService;
    private final String pageNumber = "200";

    @Override
    public Void getData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("numOfRows", pageNumber);
        CampingApiResponse response = catcherFeignService.parseService(params, CampingApiResponse.class);
        
        List<CampingApiResponse.CampingItem> campingItemList = response.getItems().getItem();
        int totalCount = response.getTotalCount();
        int numOfRows = response.getNumOfRows();
        int totalPages = (totalCount / numOfRows) + ((totalCount % numOfRows) == 0 ? 0 : 1);

        IntStream.rangeClosed(2, totalPages)
                .forEach(page -> {
                    params.put("pageNo", page);
                    params.put("numOfRows", pageNumber);
                    CampingApiResponse extraResponse = catcherFeignService.parseService(params, CampingApiResponse.class);
                    campingItemList.addAll(extraResponse.getItems().getItem());
                });

        campingService.batch(campingItemList);
        return null;
    }
}
