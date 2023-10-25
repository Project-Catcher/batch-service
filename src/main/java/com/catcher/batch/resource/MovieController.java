package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.common.service.CatcherJsonService;
import com.catcher.batch.core.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {
    private final CatcherJsonService catcherJsonService;
    private final CatcherFeignService catcherFeignService;
    private final ApiService apiService;

    @GetMapping("/webclient-batch")
    public Mono<ResponseEntity<MovieApiResponse>> getMovieDataByWebClient() {
        HashMap<String, Object> params = new HashMap<>();
        Mono<MovieApiResponse> stringMono = catcherJsonService.parseService(params, MovieApiResponse.class);

        return stringMono
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/feign-batch")
    public CommonResponse<MovieApiResponse> getMovieDataByFeign() {
        HashMap<String, Object> params = new HashMap<>();
        MovieApiResponse movieResponse = catcherFeignService.parseService(params, MovieApiResponse.class);

        return CommonResponse.success(200, movieResponse);
    }

    @GetMapping("/batch")
    public CommonResponse<MovieApiResponse> movieData() {
        MovieApiResponse response = (MovieApiResponse) apiService.getData();
        return CommonResponse.success(200, response);
    }
}