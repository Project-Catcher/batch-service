package com.catcher.batch.core.service;

import com.catcher.batch.core.converter.CatcherConverter;
import com.catcher.batch.core.properties.PropertyBase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CatcherJsonService {
    private final CatcherConverter catcherConverter;
    private final WebClient webClient;
    private final ApplicationContext applicationContext;
    private List<PropertyBase> properties;

    @PostConstruct
    void postConstruct() {
        this.properties = new ArrayList<>();
        String[] names = applicationContext.getBeanNamesForType(PropertyBase.class);
        for (String name : names) {
            PropertyBase bean = (PropertyBase) applicationContext.getBean(name);
            this.properties.add(bean);
        }
    }

    public <T> Mono<T> parseService(Map<String, Object> params, Class<T> requestType) {
        PropertyBase propertyBase = getProperty(requestType);
        propertyBase.setParams(params);

        URI uri = propertyBase.getURI();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> catcherConverter.parse(json, requestType));
    }

    private PropertyBase getProperty(Class<?> clazz) {
        return properties.stream()
                .filter(propertyBase -> propertyBase.support(clazz))
                .findFirst()
                .orElseThrow();
    }
}
