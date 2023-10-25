package com.catcher.batch.infrastructure.properties;

import com.catcher.batch.core.dto.FestivalApiResponse;
import com.catcher.batch.core.properties.PropertyBase;
import com.catcher.batch.infrastructure.utils.KmsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class FestivalProperties extends PropertyBase {
    @Value("${festival.key}")
    private String serviceKey;

    public FestivalProperties(@Value("${festival.baseUrl}") String endPoint) {
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
                .queryParam("serviceKey", KmsUtils.decrypt(serviceKey));

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
