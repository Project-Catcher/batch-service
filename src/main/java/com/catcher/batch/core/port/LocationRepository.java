package com.catcher.batch.core.port;

import com.catcher.batch.core.domain.entity.Location;

import java.util.Optional;

public interface LocationRepository {
    Optional<Location> findByDescription(String province, String city);
}
