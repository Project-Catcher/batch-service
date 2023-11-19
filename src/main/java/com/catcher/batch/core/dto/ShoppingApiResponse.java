package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@CatcherJson(path = "response.body")
public class ShoppingApiResponse {

    @JsonProperty("items")
    private ShoppingItems items;

    @JsonProperty("totalCount")
    private Integer totalCount;

    @JsonProperty("numOfRows")
    private Integer numOfRows;

    @JsonProperty("pageNo")
    private Integer pageNo;

    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShoppingItems {
        @JsonProperty("item")
        private List<ShoppingItem> item;
    }

    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShoppingItem {
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
