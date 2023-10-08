package com.catcher.batch.openapi.jsonparser.festival;

import com.catcher.batch.openapi.jsonparser.CatcherJsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.GET;

class FestivalJsonParserTest {


    @Test
    void 축제_데이터_파싱() throws JsonProcessingException {
        String headerPath = "response.header";
        String bodyPath = "response.body";
        String endPoint = "http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api";
        String serviceKey = "";

        CatcherJsonParser festivalParser = new CatcherJsonParser(headerPath, bodyPath) {
            @Override
            public String getUri(int page, int count) {
                return new StringBuilder()
                        .append(endPoint).append("?")
                        .append("serviceKey=").append(serviceKey)
                        .append("&").append("pageNo=").append(page)
                        .append("&").append("numOfRows=").append(count)
                        .append("&").append("type=").append("json").toString();
            }
        };

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        //when
        ResponseEntity<String> exchange = restTemplate.exchange(festivalParser.getUri(1, 5), GET, entity, String.class);
        String body = exchange.getBody();

        //then
        FestivalHeader festivalHeader = festivalParser.parseHeader(body, FestivalHeader.class);
        assertThat(festivalHeader.validate()).isTrue();
        FestivalBody festivalBody = festivalParser.parseBody(body, FestivalBody.class);
        assertThat(festivalBody.getItems().size()).isEqualTo(5);
    }
}