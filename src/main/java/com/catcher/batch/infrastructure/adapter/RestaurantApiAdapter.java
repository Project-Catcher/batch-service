package com.catcher.batch.infrastructure.adapter;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.core.service.ApiService;
import com.catcher.batch.core.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Qualifier("restaurant")
public class RestaurantApiAdapter implements ApiService<Void> {
    private final CatcherFeignService catcherFeignService;
    private final RestaurantService restaurantService;

    @Override
    public Void getData() {
        HashMap<String, Object> params = new HashMap<>();
        RestaurantApiResponse response = catcherFeignService.parseService(params, RestaurantApiResponse.class);

        List<RestaurantApiResponse.RestaurantItem> restaurantItemList = response.getItems().getItem();
        int totalCount = response.getTotalCount();
        int numOfRows = response.getNumOfRows();
        int totalPages = (totalCount / numOfRows) + ((totalCount % numOfRows) == 0 ? 0 : 1);

        IntStream.rangeClosed(2, totalPages)
                .parallel()
                .forEach(page -> {
                    params.put("pageNo", page);
                    RestaurantApiResponse extraResponse = catcherFeignService.parseService(params, RestaurantApiResponse.class);
                    restaurantItemList.addAll(extraResponse.getItems().getItem());
                });

        restaurantService.batch(restaurantItemList);
        return null;
    }
}
