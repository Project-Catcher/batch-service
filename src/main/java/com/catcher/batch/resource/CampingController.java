package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.common.service.CatcherFeignService;

import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.core.service.CampingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camping")
@EnableWebMvc
public class CampingController {
    private final CatcherFeignService catcherFeignService;
    private final CampingService campingService;

    @GetMapping("/feign-batch")
    public CommonResponse<CampingApiResponse> getCampingDataByFeign(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer count
    ) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("numOfRows", count);
        CampingApiResponse campingApiResponse = catcherFeignService.parseService(params, CampingApiResponse.class);

        return CommonResponse.success(200, campingApiResponse);
    }

    @PostMapping("/batch")
    public CommonResponse<Object> batchCampingData(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer count
    ) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("numOfRows", count);
        CampingApiResponse campingApiResponse = catcherFeignService.parseService(params, CampingApiResponse.class);

        return CommonResponse.success(201, null);
    }
}
