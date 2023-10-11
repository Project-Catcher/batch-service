package com.catcher.batch.infrastructure;

import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.MovieWebClientApiService;
import com.catcher.batch.core.service.MovieRestApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieWebClientApiService movieApiService;
    private final MovieRestApiService movieRestApiService;

    @GetMapping("/webclient-batch")
    public Mono<ResponseEntity<MovieApiResponse>> getMovieDataByWebClient() {

        return movieApiService.getOpenApi()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/rest-batch")
    public ResponseEntity<MovieApiResponse> getMovieDataByRest() {
        MovieApiResponse response = movieRestApiService.getOpenApi();

        return ResponseEntity.ok(response);
    }
}