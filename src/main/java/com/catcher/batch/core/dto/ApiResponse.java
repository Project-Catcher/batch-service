package com.catcher.batch.core.dto;

import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Location;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface ApiResponse {
    ZoneId zoneId = ZoneId.of("Asia/Seoul");

    ZonedDateTime getEndAt();

    String getAddress();

    String getHashString();

    String getCategory();

    CatcherItem toEntity(Location location);

    default boolean isExpired() {
        return getEndAt() != null && ZonedDateTime.now(zoneId).isAfter(getEndAt());
    }
}
