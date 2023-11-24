package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.ShoppingApiResponse;
import com.catcher.batch.core.service.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    private final CatcherFeignService catcherFeignService;
    private final ShoppingService shoppingService;

    @PostMapping("/batch")
    public CommonResponse<Object> batchShoppingData(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer count
    ) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("numOfRows", count);
        ShoppingApiResponse shoppingApiResponse = catcherFeignService.parseService(params, ShoppingApiResponse.class);

        return CommonResponse.success(201, null);
    }
}
