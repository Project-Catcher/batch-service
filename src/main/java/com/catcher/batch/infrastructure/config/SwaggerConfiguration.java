package com.catcher.batch.infrastructure.config;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("Catcher Batch Service API")
                        .description("API's using to call Batch Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("BatchService Wiki Documentation"));
//              TODO: API 문서 정리된 링크 연결 필요
//                        .url("https://springshop.wiki.github.org/docs"));
    }

}