package com.catcher.batch.core.service;

import com.catcher.batch.config.RestTemplateConfig;
import com.catcher.batch.core.converter.JsonConverter;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.infrastructure.service.KmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieRestApiService {

    @Value("${movie.baseUrl}")
    private String BASE_URL;

    @Value("${movie.key}")
    private String SERVICE_KEY;

    private final RestTemplateConfig restTemplateConfig;
    private final KmsService kmsService;

    public MovieApiResponse getOpenApi() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime previousDate = currentDateTime.minus(1, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String yesterday = previousDate.format(formatter);
        String decryptString = kmsService.decrypt(SERVICE_KEY);
        
        URI apiUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("key", decryptString)
                .queryParam("targetDt", yesterday)
                .build()
                .encode()
                .toUri();

        ResponseEntity<HashMap> responseEntity = restTemplateConfig.restTemplate().getForEntity(apiUrl, HashMap.class);
        HashMap<String, Object> map = (HashMap<String, Object>) responseEntity.getBody().get("boxOfficeResult");
        List<MovieApiResponse.MovieItemDTO> movieItemDTOList = (List<MovieApiResponse.MovieItemDTO>) map.get("dailyBoxOfficeList");
        MovieApiResponse response = new MovieApiResponse();
        response.setResponse(movieItemDTOList);

        return response;
    }
}
