package com.catcher.batch.openapi.jsonparser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public abstract class CatcherJsonParser {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String headerPath;
    private String bodyPath;

    public CatcherJsonParser(String headerPath, String bodyPath) {
        this.headerPath = headerPath;
        this.bodyPath = bodyPath;
    }

    public <H extends JsonHeader> H parseHeader(String json ,Class<H> clazz) throws JsonProcessingException {
        JSONObject headerJson = getJsonObject(json, headerPath);
        return objectMapper.readValue(headerJson.toString(), clazz);
    }

    public <B extends JsonBody> B parseBody(String json ,Class<B> clazz) throws JsonProcessingException {
        JSONObject bodyJson = getJsonObject(json, bodyPath);
        return objectMapper.readValue(bodyJson.toString(), clazz);
    }

    private JSONObject getJsonObject(String json, String path) {
        JSONObject jsonObject = new JSONObject(json);
        if (path != null) {
            String[] split = path.split("\\.");

            for (String s : split) {
                jsonObject = jsonObject.getJSONObject(s);
            }
        }
        return jsonObject;
    }

    public abstract String getUri(int page, int count);

}
