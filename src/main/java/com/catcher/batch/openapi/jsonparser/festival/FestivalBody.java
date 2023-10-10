package com.catcher.batch.openapi.jsonparser.festival;

import com.catcher.batch.openapi.jsonparser.JsonBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class FestivalBody implements JsonBody {
    @JsonProperty("items")
    private List<FestivalItem> items;
    @JsonProperty("totalCount")
    private Integer totalCount;
    @JsonProperty("numOfRows")
    private Integer numOfRows;
    @JsonProperty("pageNo")
    private Integer pageNo;
}
