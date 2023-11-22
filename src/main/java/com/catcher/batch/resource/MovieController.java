package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {
    private final ApiService apiService;

    @GetMapping("/batch")
    public CommonResponse<MovieApiResponse> movieData() {
        apiService.getData();

        return CommonResponse.success(200, null);
    }
}