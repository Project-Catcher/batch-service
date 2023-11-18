package com.catcher.batch.datasource;

import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatcherItemJpaRepository extends JpaRepository<CatcherItem, Long> {
    Optional<CatcherItem> findByItemHashValue(String itemHashValue);

    List<CatcherItem> findByCategory(Category category);
}
