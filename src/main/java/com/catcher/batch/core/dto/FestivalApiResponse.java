package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@CatcherJson(path = "response.body")
public class FestivalApiResponse {
    @JsonProperty("items")
    private List<FestivalItem> items;
    @JsonProperty("totalCount")
    private Integer totalCount;
    @JsonProperty("numOfRows")
    private Integer numOfRows;
    @JsonProperty("pageNo")
    private Integer pageNo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class FestivalItem implements ApiResponse {
        private final static String CATEGORY = "festival";

        @JsonProperty("fstvlNm")
        private String fetivalName;

        @JsonProperty("rdnmadr")
        private String address;

        @JsonProperty("fstvlCo")
        private String description;

        @JsonProperty("homepageUrl")
        private String resourceUrl;

        @JsonProperty("fstvlStartDate")
        private Date startDate;

        @JsonProperty("fstvlEndDate")
        private Date endDate;

        @Override
        public ZonedDateTime getEndAt() {
            return endDate == null ? null : endDate.toInstant().atZone(zoneId);
        }

        @Override
        public String getAddress() {
            return address;
        }

        @Override
        public String getHashString() {
            return CATEGORY + "-" + fetivalName;
        }

        @Override
        public String getCategory() {
            return CATEGORY;
        }

        @Override
        public CatcherItem toEntity(Location location) {
            return CatcherItem
                    .builder()
                    .itemHashValue(getHashString())
                    .startAt(startDate.toInstant().atZone(zoneId))
                    .resourceUrl(resourceUrl)
                    .description(description)
                    .build();
        }
    }
}
