package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@CatcherJson(path = "")
public class RestaurantApiResponse {

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("documents")
    private List<RestaurantItem> items;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("is_end")
        private boolean isEnd;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestaurantItem {

        @JsonProperty("id")
        private String key;

        @JsonProperty("place_name")
        private String name;

        @JsonProperty("place_url")
        private String resourceUrl;

        @JsonProperty("address_name")
        private String address;

        @JsonProperty("x")
        private String latitude;

        @JsonProperty("y")
        private String longitude;
    }
}
