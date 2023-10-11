package com.catcher.batch.resource;

import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.MovieRestApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieRestApiService movieRestApiService;

    @GetMapping("/rest-batch")
    public ResponseEntity<MovieApiResponse> getMovieDataByRest() {
        MovieApiResponse response = movieRestApiService.getOpenApi();

        return ResponseEntity.ok(response);
    }
}