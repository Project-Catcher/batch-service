package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final ApiService apiService;
    @PostMapping("/batch")
    public CommonResponse<Object> batchRestaurantData() {
        apiService.getData();

        return CommonResponse.success(201, null);
    }
}
