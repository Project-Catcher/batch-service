package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class FestivalItem {
        @JsonProperty("fstvlNm")
        private String name;

        @JsonProperty("rdnmadr")
        private String address;

        @JsonProperty("fstvlStartDate")
        private Date startDate;

        @JsonProperty("fstvlEndDate")
        private Date endDate;
    }
}
