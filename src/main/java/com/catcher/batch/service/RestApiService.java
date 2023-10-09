package com.catcher.batch.service;

import com.catcher.batch.core.config.RestTemplateConfig;
import com.catcher.batch.core.dto.CampingApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class RestApiService {

    // TODO 하드코딩 수정
    private final String BASE_URL = "https://apis.data.go.kr/B551011/GoCamping";

    @Value("${catcher.api.key}")
    private String SERVICE_KEY;

    private final RestTemplateConfig restTemplateConfig;

    public CampingApiResponse getOpenApi() {
        URI apiUrl = UriComponentsBuilder
                .fromUriString(BASE_URL).path("/basedList")
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("_type", "json")
                .build()
                .encode()
                .toUri();

        ResponseEntity<CampingApiResponse> responseEntity = restTemplateConfig.restTemplate().getForEntity(apiUrl, CampingApiResponse.class);
        return responseEntity.getBody();
    }
}
