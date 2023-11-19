package com.catcher.batch.datasource;

import com.catcher.batch.core.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationJpaRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByDescriptionStartingWithAndDescriptionEndingWith(String start, String end);

    Optional<Location> findByAreaCode(String areaCode);

}
