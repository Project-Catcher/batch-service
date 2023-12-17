package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.service.ApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    RestaurantController(@Qualifier("restaurant") ApiService apiService) {
        this.apiService = apiService;
    }

    private final ApiService apiService;

    @GetMapping("/batch")
    public CommonResponse<Object> batchRestaurantData() {
        apiService.getData();

        return CommonResponse.success(201, null);
    }
}
