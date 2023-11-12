package com.catcher.batch.datasource;

import com.catcher.batch.core.domain.entity.CatcherItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatcherItemJpaRepository extends JpaRepository<CatcherItem, Long> {
    Optional<CatcherItem> findByItemHashValue(String itemHashValue);
}
