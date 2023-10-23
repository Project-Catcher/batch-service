package com.catcher.batch.core.service;


import com.catcher.batch.infrastructure.MovieApiAdapter;
import com.catcher.batch.core.dto.MovieApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieApiAdapter movieApiAdapter;

    public MovieApiResponse getMovieData() {
        return movieApiAdapter.getMovieDataByApi();

    }
}