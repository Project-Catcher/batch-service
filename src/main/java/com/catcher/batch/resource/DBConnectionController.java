package com.catcher.batch.resource;

import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db/test")
@RequiredArgsConstructor
@Slf4j
public class DBConnectionController {
    // TODO : 삭제 예정
    private final CategoryRepository categoryRepository;

    @GetMapping("/{categoryName}")
    public String category(@PathVariable String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElse(null);

        log.info("Category = {}", category.getName());

        return category == null ? "카테고리를 찾을 수 없습니다" : category.getName();
    }
}
