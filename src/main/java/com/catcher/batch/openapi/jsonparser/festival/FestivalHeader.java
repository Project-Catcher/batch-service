package com.catcher.batch.openapi.jsonparser.festival;

import com.catcher.batch.openapi.jsonparser.JsonHeader;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class FestivalHeader implements JsonHeader {
    @JsonProperty("resultCode")
    private String resultCode;
    @JsonProperty("resultMsg")
    private String resultMsg;
    @JsonProperty("type")
    private String type;

    @Override
    public boolean validate() {
        return resultCode.equals("00");
    }
}
