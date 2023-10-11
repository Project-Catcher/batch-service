package com.catcher.batch.core.properties;

import com.catcher.batch.core.dto.FestivalApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class FestivalProperties extends PropertyBase{
    @Value("${json.url.festival.key}")
    private String serviceKey;

    public FestivalProperties(@Value("${json.url.festival.endpoint}") String endPoint) {
        super(endPoint);
    }

    @Override
    public boolean support(Class<?> clazz) {
        return clazz.isAssignableFrom(FestivalApiResponse.class);
    }

    @Override
    public URI getURI() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(this.getEndPoint())
                .queryParam("serviceKey", serviceKey);

        return this.addParams(uriBuilder)
                .build().toUri();
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
