package com.catcher.batch.datasource;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CatcherItemRepositoryImpl implements CatcherItemRepository {
    private final CatcherItemJpaRepository catcherItemJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveAll(List<CatcherItem> catcherItems) {
        catcherItemJpaRepository.saveAll(catcherItems);
    }

    @Override
    public void save(CatcherItem catcherItem) {
        catcherItemJpaRepository.save(catcherItem);
    }

    @Override
    public Optional<CatcherItem> findByItemHashValue(String hashKey) {
        return catcherItemJpaRepository.findByItemHashValue(hashKey);
    }

    @Override
    public List<CatcherItem> findByCategory(Category category) {
        return catcherItemJpaRepository.findByCategory(category);
    }

    @Override
    public void deleteAll(List<CatcherItem> catcherItems) {
        for(var item: catcherItems){
            item.softDelete();
        }
        catcherItemJpaRepository.saveAll(catcherItems);
    }

    @Override
    public void refreshUpdateDate(List<String> itemHashValueList) {
        entityManager.createQuery("UPDATE CatcherItem c SET c.updatedAt = :curTime WHERE c.itemHashValue IN :itemHashValueList")
                .setParameter("itemHashValueList", itemHashValueList)
                .setParameter("curTime", ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
                .executeUpdate();
    }
}
