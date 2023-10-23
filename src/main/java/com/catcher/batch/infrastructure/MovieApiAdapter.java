package com.catcher.batch.infrastructure;

import com.catcher.batch.core.converter.CatcherConverter;
import com.catcher.batch.core.dto.MovieApiResponse;
import com.catcher.batch.infrastructure.utils.KmsUtils;
import com.catcher.batch.resource.config.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class MovieApiAdapter {

    @Value("${movie.baseUrl}")
    private String BASE_URL;

    @Value("${movie.key}")
    private String SERVICE_KEY;

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Autowired
    private KmsUtils kmsUtils;

    public MovieApiResponse getMovieDataByApi(){
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime previousDate = currentDateTime.minus(1, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String yesterday = previousDate.format(formatter);
        String decryptString = kmsUtils.decrypt(SERVICE_KEY);

        URI apiUrl = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("key", decryptString)
                .queryParam("targetDt", yesterday)
                .queryParam("itemPerPage", 50)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplateConfig.restTemplate().getForEntity(apiUrl, String.class);
        CatcherConverter<MovieApiResponse> movieConverter = new CatcherConverter<>(MovieApiResponse.class, "boxOfficeResult");

        return movieConverter.parse(responseEntity.getBody());
    }
}
