package com.catcher.batch.core.database;

import com.catcher.batch.core.domain.entity.CatcherItem;

import java.util.List;
import java.util.Optional;

public interface CatcherItemRepository {
    void saveAll(List<CatcherItem> catcherItems);

    Optional<CatcherItem> findByItemHashValue(String hashKey);
}
