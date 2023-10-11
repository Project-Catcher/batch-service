package com.catcher.batch.external.config;

import com.catcher.batch.core.converter.CatcherConverter;
import feign.RequestInterceptor;
import feign.ResponseInterceptor;
import feign.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CatcherFeignClientCommonConfig {

    public static final String APPLICATION_FORM_URLENCODED_UTF8_VALUE =
            MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8";

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> requestTemplate.header(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED_UTF8_VALUE);
    }

    @Bean
    public ResponseInterceptor responseInterceptor(CatcherConverter catcherConverter) {

        return invocationContext -> {
            Class<?> responseType = (Class<?>) invocationContext.returnType();
            String responseBody = Util.toString(invocationContext.response().body().asReader());
            return catcherConverter.parse(responseBody, responseType);
        };
    }
}
