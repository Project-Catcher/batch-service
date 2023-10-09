package com.catcher.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieController {

//    @Value("${movie.baseUrl}")
    private final String boxOfficeUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
//    @Value("${movie.key}")
    //KMS로 암호화한 데이터
    private final String keyValue = "AQICAHg2j0pl3GguoQNkjcreuBPYJqI3OPeLEOelKIkMSzn01AFKU5xBtnKOYNJoV9SPqiJJAAAAfjB8BgkqhkiG9w0BBwagbzBtAgEAMGgGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMQztverYmF4UG+r1CAgEQgDvBt51FsjGNLPjfGR7ykLP38eg1Sh3mS78PY2TH2UFbnThy1+Vw+D5/R6U2e7Iwl6P0Ww+AyGHBEX437Q==";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/batch")
    public ResponseEntity<Object> getMovieDataByAPI() throws JsonProcessingException {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        ZonedDateTime previousDate = currentDateTime.minus(1, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String yesterday = previousDate.format(formatter);

        String url = boxOfficeUrl + "?key=" + keyValue + "&targetDt="+yesterday;

        ResponseEntity<HashMap<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        HashMap<String, Object> resultMap = response.getBody();
        Object obj = response.getBody().get("boxOfficeResult");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);

        Map<String, Object> dataMap = objectMapper.readValue(json, Map.class);

        List<Map<String, Object>> dailyBoxOfficeList = (List<Map<String, Object>>) dataMap.get("dailyBoxOfficeList");

        for (Map<String, Object> entry : dailyBoxOfficeList) {
            System.out.println("영화 이름: " + entry.get("movieNm"));
            System.out.println("순위: " + entry.get("rank"));
            System.out.println("영화 코드: "+entry.get("movieCd"));
            System.out.println("영화 개봉일: "+entry.get("openDt"));
            System.out.println("누적 관객수: " + entry.get("audiAcc"));
            System.out.println("---------------------------");
        }

        return ResponseEntity.ok(response);
    }

}