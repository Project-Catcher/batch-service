package com.catcher.batch.openapi.jsonparser.festival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class FestivalItem {
    @JsonProperty("fstvlNm")
    private String name;

    @JsonProperty("rdnmadr")
    private String address;

    @JsonProperty("fstvlStartDate")
    private Date startDate;

    @JsonProperty("fstvlEndDate")
    private Date endDate;
}
