package com.catcher.batch.datasource;

import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
    private final LocationJpaRepository locationJpaRepository;

    @Override
    public Optional<Location> findByDescription(String start, String end) {
        return locationJpaRepository.findByDescriptionStartingWithAndDescriptionEndingWith(start, end);
    }
}
