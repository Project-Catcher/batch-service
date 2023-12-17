package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.service.ApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequestMapping("/camping")
@EnableWebMvc
public class CampingController {
    CampingController(@Qualifier("camping") ApiService apiService) {
        this.apiService = apiService;
    }

    private final ApiService apiService;

    @GetMapping("/batch")
    public CommonResponse<Object> batchCampingData() {
        apiService.getData();

        return CommonResponse.success(201, null);
    }
}
