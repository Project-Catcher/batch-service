package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
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
        public String getCategory() {
            return "festival";
        }

        @Override
        public String getHashValue() {
            return fetivalName;
        }

        @Override
        public String getTitle() {
            return fetivalName;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getThumbnailUrl() {
            return null;
        }

        @Override
        public String getResourceUrl() {
            return resourceUrl;
        }

        @Override
        public ZonedDateTime getStartAt() {
            return startDate.toInstant().atZone(zoneId);
        }

        @Override
        public ZonedDateTime getEndAt() {
            return endDate.toInstant().atZone(zoneId);
        }

        @Override
        public String getAddress() {
            return address;
        }

        @Override
        public String getProvince() {
            return null;
        }

        @Override
        public String getCity() {
            return null;
        }
    }
}
