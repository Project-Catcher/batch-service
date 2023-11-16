package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.dto.MovieApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.catcher.batch.common.utils.HashCodeGenerator.hashString;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    public static final String CATEGORY_NAME = "movie";

    @Transactional
    public void batch(List<MovieApiResponse.MovieItem> movies){

        Category category = categoryRepository.findByName(CATEGORY_NAME)
                .orElseGet(()->categoryRepository.save(Category.create(CATEGORY_NAME)));

        Map<String, CatcherItem> itemHashKeyToDBItem = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, Function.identity()));


        //TODO: 썸네일URL, 개봉일 변경 여부 확인 로직 필요

//        Map<String, MovieApiResponse.MovieItem> itemHashKeyToApiItem = movies.stream()
//                .collect(Collectors.toMap(
//                        movieItem -> hashString(CATEGORY_NAME, String.valueOf(movieItem.getId())),
//                        movieItem -> movieItem
//                ));

//        List<CatcherItem> catcherItems = itemHashKeyToApiItem.entrySet().stream()
//                .map(entry -> {
//                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
//                    LocalDate localDate = LocalDate.parse(entry.getValue().getReleaseDate(), dateFormatter);
//
//                    ZonedDateTime releaseDate = localDate.atStartOfDay(ZoneId.systemDefault());
//
//                    return CatcherItem.builder()
//                            .category(category)
//                            .title(entry.getValue().getTitle())
//                            .itemHashValue(entry.getKey())
//                            .thumbnailUrl(entry.getValue().getPosterPath())
//                            .startAt(releaseDate)
//                            .build();
//                })
//                .collect(Collectors.toList());
//DB에 추가
//        catcherItemRepository.saveAll(catcherItems);
    }
}
