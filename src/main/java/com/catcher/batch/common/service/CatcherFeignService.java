package com.catcher.batch.common.service;

import com.catcher.batch.core.converter.CatcherConverter;
import com.catcher.batch.core.properties.PropertyBase;
import com.catcher.batch.core.properties.HeaderSupport;
import com.catcher.batch.resource.external.ExternalFeign;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatcherFeignService {
    private final ExternalFeign externalFeign;
    private final CatcherConverter catcherConverter;
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

    @Async
    public <T> T parseService(Map<String, Object> params, Class<T> requestType) {
        PropertyBase property = getProperty(requestType);
        property.setParams(params);
        URI uri = property.getURI();

        // 헤더가 있는 경우
        HttpHeaders headers;
        if (property instanceof HeaderSupport headerSupport) {
            headers = headerSupport.addHeaders();
            return catcherConverter.parse(externalFeign.getInfoWithHeader(headers.getFirst("Authorization"), uri), requestType);
        }

        return catcherConverter.parse(externalFeign.getInfo(uri), requestType);
    }

    private PropertyBase getProperty(Class<?> clazz) {
        return properties.stream()
                .filter(propertyBase -> propertyBase.support(clazz))
                .findFirst()
                .orElseThrow();
    }
}
