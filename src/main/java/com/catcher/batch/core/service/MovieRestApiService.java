package com.catcher.batch.core.service;


import com.catcher.batch.infrastructure.KmsUtils;
import com.catcher.batch.resource.config.RestTemplateConfig;
import com.catcher.batch.core.converter.CatcherConverter;
import com.catcher.batch.core.dto.MovieApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MovieRestApiService {

    @Value("${movie.baseUrl}")
    private String BASE_URL;

    @Value("${movie.key}")
    private String SERVICE_KEY;

    private final RestTemplateConfig restTemplateConfig;
    private final KmsUtils kmsUtils;

    public MovieApiResponse getOpenApi() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime previousDate = currentDateTime.minus(1, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String yesterday = previousDate.format(formatter);
        String decryptString = kmsUtils.decrypt(SERVICE_KEY);

        URI apiUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("key", decryptString)
                .queryParam("targetDt", yesterday)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplateConfig.restTemplate().getForEntity(apiUrl, String.class);
        CatcherConverter<MovieApiResponse> movieConverter = new CatcherConverter<>(MovieApiResponse.class, "boxOfficeResult");
        return movieConverter.parse(responseEntity.getBody());
    }
}