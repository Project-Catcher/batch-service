package com.catcher.batch.core.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface ApiResponse {
    ZoneId zoneId = ZoneId.of("Asia/Seoul");

    String getCategory();
    String getHashValue();
    String getTitle();
    String getDescription();
    String getThumbnailUrl();
    String getResourceUrl();
    ZonedDateTime getStartAt();
    ZonedDateTime getEndAt();
    String getAddress();
    String getProvince();
    String getCity();
}
