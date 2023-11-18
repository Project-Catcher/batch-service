package com.catcher.batch.infrastructure.properties;

import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.core.properties.HeaderSupport;
import com.catcher.batch.core.properties.PropertyBase;
import com.catcher.batch.infrastructure.utils.KmsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class RestaurantProperties extends PropertyBase implements HeaderSupport {

    @Value("${restaurant.key}")
    private String serviceKey;

    public RestaurantProperties(@Value("${restaurant.baseUrl}") String endPoint) {
        super(endPoint);
    }

    @Override
    public boolean support(Class<?> clazz) {
        return clazz.isAssignableFrom(RestaurantApiResponse.class);
    }

    @Override
    public URI getURI() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(this.getEndPoint())
                .queryParam("category_group_code", "FD6");

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

    @Override
    public HttpHeaders addHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KmsUtils.decrypt(serviceKey));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
