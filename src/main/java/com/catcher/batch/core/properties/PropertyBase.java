package com.catcher.batch.core.properties;

import lombok.Getter;

import java.net.URI;
import java.util.Map;

@Getter
public abstract class PropertyBase {
    private String endPoint;
    private Map<String, Object> params;

    public PropertyBase(String endPoint) {
        this.endPoint = endPoint;
    }

    public abstract boolean support(Class<?> clazz);

    public abstract URI getURI();

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
