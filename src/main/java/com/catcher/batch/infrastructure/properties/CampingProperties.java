package com.catcher.batch.infrastructure.properties;

import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.core.properties.PropertyBase;
import com.catcher.batch.infrastructure.utils.KmsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class CampingProperties extends PropertyBase {

    @Value("${camping.key}")
    private String serviceKey;

    public CampingProperties(@Value("${camping.baseUrl}") String endPoint) {
        super(endPoint);
    }

    @Override
    public boolean support(Class<?> clazz) {
        return clazz.isAssignableFrom(CampingApiResponse.class);
    }

    @Override
    public URI getURI() {
        try {
            String key = URLEncoder.encode(KmsUtils.decrypt(serviceKey), "UTF-8");

            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(this.getEndPoint())
                    .queryParam("MobileOS", "ETC")
                    .queryParam("MobileApp", "AppTest")
                    .queryParam("serviceKey", key)
                    .queryParam("_type", "json");

            return this.addParams(uriBuilder)
                    .build(true).toUri();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private UriComponentsBuilder addParams(UriComponentsBuilder uriComponentsBuilder ) {
        Map<String, Object> params = getParams();
        for (String key : params.keySet()) {
            uriComponentsBuilder
                    .queryParam(key, params.get(key));
        }
        return uriComponentsBuilder;
    }
}
