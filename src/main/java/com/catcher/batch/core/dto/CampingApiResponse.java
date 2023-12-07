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
    public static class CampingItem implements ApiResponse {
        private final static String CATEGORY = "camping";

        @JsonProperty("facltNm")
        private String name;

        @JsonProperty("contentId")
        private String key;

        @JsonProperty("lineIntro")
        private String description;

        @JsonProperty("addr1")
        private String address;

        @JsonProperty("induty")
        private String category;

        @JsonProperty("homepage")
        private String homepage;

        @JsonProperty("firstImageUrl")
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
                    .resourceUrl(homepage)
                    .description(description)
                    .location(location)
                    .category(category)
                    .build();
        }
    }
}
