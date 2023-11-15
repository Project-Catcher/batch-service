package com.catcher.batch.datasource;

import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.domain.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }
}
