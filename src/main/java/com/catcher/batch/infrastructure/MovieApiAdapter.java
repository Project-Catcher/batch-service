package com.catcher.batch.infrastructure;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.core.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MovieApiAdapter implements ApiService<MovieApiResponse> {

    private final CatcherFeignService catcherFeignService;

    @Override
    public MovieApiResponse getData() {
        HashMap<String, Object> params = new HashMap<>();
        return catcherFeignService.parseService(params, MovieApiResponse.class);
    }
}
