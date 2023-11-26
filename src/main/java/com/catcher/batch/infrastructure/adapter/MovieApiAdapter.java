package com.catcher.batch.infrastructure.adapter;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.ApiService;
import com.catcher.batch.core.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Qualifier("movie")
public class MovieApiAdapter implements ApiService<Void> {

    private final CatcherFeignService catcherFeignService;
    private final MovieService movieService;

    @Override
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public Void getData() {
        HashMap<String, Object> params = new HashMap<>();
        MovieApiResponse response = catcherFeignService.parseService(params, MovieApiResponse.class);

        List<MovieApiResponse.MovieItem> movieList = response.getItems();
        int totalPages = response.getTotalPages();

        IntStream.rangeClosed(2, totalPages)
                .parallel()
                .forEach(page -> {
                    params.put("page", page);
                    MovieApiResponse extraResponse = catcherFeignService.parseService(params, MovieApiResponse.class);
                    movieList.addAll(extraResponse.getItems());
                });

        movieService.batch(movieList);

        return null;
    }
}
