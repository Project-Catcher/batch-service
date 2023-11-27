package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.catcher.batch.common.utils.HashCodeGenerator;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

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
    public static class FestivalItem implements ApiResponse {
        private final static String CATEGORY = "festival";

        @JsonProperty("fstvlNm")
        private String festivalName;

        @JsonProperty("rdnmadr")
        private String roadAddress;

        @JsonProperty("lnmadr")
        private String zibunAddress;

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
            return StringUtils.isBlank(roadAddress) ? zibunAddress : roadAddress;
        }

        @Override
        public String getHashString() {
            return HashCodeGenerator.hashString(CATEGORY, festivalName, startDate, endDate);
        }

        @Override
        public String getCategory() {
            return CATEGORY;
        }

        @Override
        public CatcherItem toEntity(Location location, Category category) {
            return CatcherItem
                    .builder()
                    .title(festivalName)
                    .itemHashValue(getHashString())
                    .startAt(startDate.toInstant().atZone(zoneId))
                    .resourceUrl(resourceUrl)
                    .description(description)
                    .endAt(getEndAt())
                    .location(location)
                    .category(category)
                    .build();
        }
    }
}
