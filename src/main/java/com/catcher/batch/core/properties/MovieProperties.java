package com.catcher.batch.core.properties;

import com.catcher.batch.core.dto.MovieApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class MovieProperties extends PropertyBase {

    @Value("${json.url.movie.key}")
    private String serviceKey;

    private String targetDate;

    public MovieProperties(@Value("${json.url.movie.endpoint}") String endPoint) {
        super(endPoint);
    }

    @Override
    public boolean support(Class<?> clazz) {
        if (clazz.isAssignableFrom(MovieApiResponse.class)) {
            initCurrentDate();
            return true;
        }
        return false;
    }

    @Override
    public URI getURI() {
        return UriComponentsBuilder
                .fromUriString(this.getEndPoint())
                .queryParam("key", serviceKey)
                .queryParam("targetDt", targetDate)
                .build().toUri();
    }

    private void initCurrentDate() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime previousDate = currentDateTime.minus(1, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        targetDate = previousDate.format(formatter);
    }
}
