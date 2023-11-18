package com.catcher.batch.core.database;

import com.catcher.batch.core.domain.entity.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);

    Category save(Category category);
}
