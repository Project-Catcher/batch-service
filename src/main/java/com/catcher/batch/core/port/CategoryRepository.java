package com.catcher.batch.core.port;

import com.catcher.batch.core.domain.entity.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);

    Category save(Category category);
}
