package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.MovieService;
import com.catcher.batch.common.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/batch")
    public CommonResponse<MovieApiResponse> movieData() {
MovieApiResponse response = movieService.getMovieData();
        return ApiUtils.success(200, response);
    }
}
