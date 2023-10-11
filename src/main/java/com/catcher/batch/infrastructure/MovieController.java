package com.catcher.batch.infrastructure;

import com.catcher.batch.core.dto.MovieApiResponse;
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

    @GetMapping("/webclient-batch")
    public Mono<ResponseEntity<MovieApiResponse>> getMovieDataByWebClient() {
        HashMap<String, Object> params = new HashMap<>();
        Mono<MovieApiResponse> stringMono = catcherJsonService.parseService(params, MovieApiResponse.class);

        return stringMono
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}