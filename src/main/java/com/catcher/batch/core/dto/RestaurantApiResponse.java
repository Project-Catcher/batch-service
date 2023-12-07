package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.catcher.batch.common.utils.HashCodeGenerator;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.ZonedDateTime;
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
    public static class RestaurantItem implements ApiResponse {
        private final static String CATEGORY = "restaurant";

        @JsonProperty("title")
        private String name;

        @JsonProperty("addr1")
        private String address;

        @JsonProperty("contentid")
        private String key;

        @JsonProperty("firstimage")
        private String thumbnailUrl;

        @Override
        public ZonedDateTime getEndAt() {
            return null;
        }

        @Override
        public String getHashString() {
            return HashCodeGenerator.hashString(CATEGORY, key);
        }

        @Override
        public String getCategory() {
            return CATEGORY;
        }

        @Override
        public CatcherItem toEntity(Location location, Category category) {
            return CatcherItem
                    .builder()
                    .title(name)
                    .itemHashValue(getHashString())
                    .thumbnailUrl(thumbnailUrl)
                    .location(location)
                    .category(category)
                    .build();
        }
    }
}
