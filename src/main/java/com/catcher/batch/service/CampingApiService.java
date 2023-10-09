package com.catcher.batch.service;

import com.catcher.batch.JsonConverter;
import com.catcher.batch.core.config.WebClientConfig;
import com.catcher.batch.core.dto.CampingApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CampingApiService {

    @Value("${catcher.api.key}")
    private String SERVICE_KEY;

    private final JsonConverter jsonConverter;
    private final WebClientConfig webClientConfig;

    public Mono<CampingApiResponse> getOpenApi() {
        return webClientConfig.webClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/basedList")
                        .queryParam("serviceKey", SERVICE_KEY)
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "AppTest")
                        .queryParam("_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> jsonConverter.jsonToObject(json, CampingApiResponse.class));
    }
}
