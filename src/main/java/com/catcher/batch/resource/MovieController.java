package com.catcher.batch.resource;

import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.CatcherFeignService;
import com.catcher.batch.core.service.CatcherJsonService;
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


    @GetMapping("/webclient-batch")
    public Mono<ResponseEntity<MovieApiResponse>> getMovieDataByWebClient() {
        HashMap<String, Object> params = new HashMap<>();
        Mono<MovieApiResponse> stringMono = catcherJsonService.parseService(params, MovieApiResponse.class);

        return stringMono
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/feign-batch")
    public ResponseEntity<MovieApiResponse> getMovieDataByFeign() {
        HashMap<String, Object> params = new HashMap<>();
        MovieApiResponse movieResponse = catcherFeignService.parseService(params, MovieApiResponse.class);

        return ResponseEntity.ok(movieResponse);
    }
}