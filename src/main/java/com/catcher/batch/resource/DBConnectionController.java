package com.catcher.batch.resource;

import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.datasource.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final CategoryJpaRepository categoryRepository;

    @GetMapping
    @Scheduled(fixedDelay = 180000L)
    public String category() {
        Category category = categoryRepository.findById(2L).orElse(null);

        return category.getName();
    }
}
