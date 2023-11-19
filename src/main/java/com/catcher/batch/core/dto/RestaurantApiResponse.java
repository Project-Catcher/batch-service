package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@CatcherJson(path = "response.body")
public class RestaurantApiResponse {

    @JsonProperty("items")
    private RestaurantItems items;

    @JsonProperty("totalCount")
    private Integer totalCount;

    @JsonProperty("numOfRows")
    private Integer numOfRows;

    @JsonProperty("pageNo")
    private Integer pageNo;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestaurantItems {
        @JsonProperty("item")
        private List<RestaurantItem> item;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestaurantItem {

        @JsonProperty("title")
        private String name;

        @JsonProperty("addr1")
        private String address;

        @JsonProperty("contentid")
        private String key;

        @JsonProperty("firstimage")
        private String thumbnailUrl;
    }
}
