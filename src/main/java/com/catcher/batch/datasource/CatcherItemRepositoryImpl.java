package com.catcher.batch.datasource;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CatcherItemRepositoryImpl implements CatcherItemRepository {
    private final CatcherItemJpaRepository catcherItemJpaRepository;

    @Override
    public void saveAll(List<CatcherItem> catcherItems) {
        catcherItemJpaRepository.saveAll(catcherItems);
    }

    @Override
    public Optional<CatcherItem> findByItemHashValue(String hashKey) {
        return catcherItemJpaRepository.findByItemHashValue(hashKey);
    }
}
