package com.catcher.batch.core.database;

import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CatcherItemRepository {
    void saveAll(List<CatcherItem> catcherItems);

    void save(CatcherItem catcherItem);

    Optional<CatcherItem> findByItemHashValue(String hashKey);

    List<CatcherItem> findByCategory(Category category);

    void deleteAll(List<CatcherItem> catcherItems);

    void refreshUpdateDate(List<String> itemHashValueList);
}
