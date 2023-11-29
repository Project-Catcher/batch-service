package com.catcher.batch.infrastructure.adapter;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.ShoppingApiResponse;
import com.catcher.batch.core.service.ApiService;
import com.catcher.batch.core.service.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Qualifier("shopping")
public class ShoppingApiAdapter implements ApiService<Void> {
    private final CatcherFeignService catcherFeignService;
    private final ShoppingService shoppingService;
    private final String pageNumber = "2000";

    @Override
    public Void getData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("numOfRows", pageNumber);
        ShoppingApiResponse response = catcherFeignService.parseService(params, ShoppingApiResponse.class);

        List<ShoppingApiResponse.ShoppingItem> shoppingItemList = response.getItems().getItem();
        int totalCount = response.getTotalCount();
        int numOfRows = response.getNumOfRows();
        int totalPages = (totalCount / numOfRows) + ((totalCount % numOfRows) == 0 ? 0 : 1);

        IntStream.rangeClosed(2, totalPages)
                .forEach(page -> {
                    params.put("pageNo", page);
                    params.put("numOfRows", pageNumber);
                    ShoppingApiResponse extraResponse = catcherFeignService.parseService(params, ShoppingApiResponse.class);
                    shoppingItemList.addAll(extraResponse.getItems().getItem());
                });

        shoppingService.batch(shoppingItemList);
        return null;
    }
}
