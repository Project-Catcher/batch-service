package com.catcher.batch.core.converter;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CatcherConverter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T parse(String jsonMessage, Class<T> responseType) {
        String path = getPath(responseType);
        JSONObject jsonObject = getJsonObject(jsonMessage, path);

        try {
            return objectMapper.readValue(jsonObject.toString(), responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String getPath(Class<T> responseType) {

        CatcherJson annotation = responseType.getAnnotation(CatcherJson.class);
        if(annotation == null) {
            throw new IllegalStateException();
        }
        return annotation.path();
    }

    private JSONObject getJsonObject(String json, String jsonPath) {
        if(jsonPath == null) {
            throw new IllegalStateException();
        } else if (StringUtils.isBlank(jsonPath)) {
            return new JSONObject(json);
        }

        JSONObject jsonObject = new JSONObject(json);
        String[] subPath = jsonPath.split("\\.");

        for (String path : subPath) {
            jsonObject = jsonObject.getJSONObject(path);
        }
        return jsonObject;
    }
}
