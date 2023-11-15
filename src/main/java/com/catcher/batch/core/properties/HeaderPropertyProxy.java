package com.catcher.batch.core.properties;

import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.util.Map;

public class HeaderPropertyProxy extends PropertyBase {
    private final PropertyBase property;

    public HeaderPropertyProxy(PropertyBase property) {
        super(property.getEndPoint());
        this.property = property;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return property.support(clazz);
    }

    @Override
    public URI getURI() {
        return property.getURI();
    }

    @Override
    public void setParams(Map<String, Object> params) {
        property.setParams(params);
    }

    public HttpHeaders addHeaders() {
        if (property instanceof HeaderSupport) {
            return ((HeaderSupport) property).addHeaders();
        }
        return new HttpHeaders();
    }
}
