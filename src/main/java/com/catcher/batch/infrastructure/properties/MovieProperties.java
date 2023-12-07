package com.catcher.batch.infrastructure.properties;

import com.catcher.batch.core.dto.MovieApiResponse;
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
public class MovieProperties extends PropertyBase implements HeaderSupport {

    @Value("${movie.key}")
    private String serviceKey;

    public MovieProperties(@Value("${movie.baseUrl}") String endPoint) {
        super(endPoint);
    }

    @Override
    public boolean support(Class<?> clazz) {
        return clazz.isAssignableFrom(MovieApiResponse.class);
    }

    @Override
    public URI getURI() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(this.getEndPoint())
                .queryParam("language", "ko-KR");

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
        headers.set("Authorization", "Bearer " + KmsUtils.decrypt(serviceKey));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
