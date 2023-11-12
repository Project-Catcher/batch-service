package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@CatcherJson(path = "response.body")
public class CampingApiResponse {

    @JsonProperty("items")
    private CampingItems items;

    @JsonProperty("totalCount")
    private Integer totalCount;

    @JsonProperty("numOfRows")
    private Integer numOfRows;

    @JsonProperty("pageNo")
    private Integer pageNo;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CampingItems {
        @JsonProperty("item")
        private List<CampingItem> item;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CampingItem {

        @JsonProperty("facltNm")
        private String name;

        @JsonProperty("contentId")
        private String key;

        @JsonProperty("lineIntro")
        private String description;

        @JsonProperty("doNm")
        private String province;

        @JsonProperty("sigunguNm")
        private String city;

        @JsonProperty("addr1")
        private String address;

        @JsonProperty("induty")
        private String category;

        @JsonProperty("zipcode")
        private String zipcode;

        @JsonProperty("homepage")
        private String homepage;

        @JsonProperty("mapX")
        private String latitude;

        @JsonProperty("mapY")
        private String longitude;

        @JsonProperty("firstImageUrl")
        private String thumbnailUrl;
    }
}
