package com.catcher.batch.core.converter;

import com.catcher.batch.core.dto.MovieApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class CatcherConverter<T> {
    private final String headerPath;
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    public CatcherConverter(Class<T> responseType, String headerPath) {
        this.clazz = responseType;
        this.headerPath = headerPath;
        objectMapper = new ObjectMapper();
    }

    public T parse(String jsonMessage) {
        JSONObject jsonObject = getJsonObject(jsonMessage);

        try {
            return objectMapper.readValue(jsonObject.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getJsonObject(String json) {
        if(headerPath == null) {
            throw new IllegalStateException();
        }

        JSONObject jsonObject = new JSONObject(json);
        String[] subPath = headerPath.split("\\.");

        for (String path : subPath) {
            jsonObject = jsonObject.getJSONObject(path);
        }
        return jsonObject;
    }
}
