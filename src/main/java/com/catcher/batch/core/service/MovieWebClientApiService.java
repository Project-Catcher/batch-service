package com.catcher.batch.core.service;

import com.catcher.batch.config.WebClientConfig;
import com.catcher.batch.core.converter.JsonConverter;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.infrastructure.service.KmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MovieWebClientApiService {

    @Value("${movie.baseUrl}")
    private String BASE_URL;

    @Value("${movie.key}")
    private String SERVICE_KEY;

    private final JsonConverter jsonConverter;
    private final WebClientConfig webClientConfig;
    private final KmsService kmsService;

    public Mono<MovieApiResponse> getOpenApi() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime previousDate = currentDateTime.minus(1, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String yesterday = previousDate.format(formatter);
        String decryptString = kmsService.decrypt(SERVICE_KEY);
        String uri = BASE_URL + String.format("?key=%s&targetDt=%s", decryptString, yesterday);

        /*정상동작 하지 않음*/
        return webClientConfig.webClient().get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> jsonConverter.jsonToObject(json, MovieApiResponse.class));
    }
}
