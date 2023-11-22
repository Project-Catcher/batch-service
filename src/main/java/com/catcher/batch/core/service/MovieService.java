package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.dto.ApiResponse;
import com.catcher.batch.core.dto.MovieApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void batch(List<? extends ApiResponse> responses) {
        if(responses.isEmpty()) return;

        Category category = categoryRepository.findByName(responses.get(0).getCategory())
                .orElseGet(() -> categoryRepository.save(Category.create(responses.get(0).getCategory())));

        Map<String, CatcherItem> itemHashKeyToDBItem = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, Function.identity()));

        List<CatcherItem> deleteList = itemHashKeyToDBItem.values().stream()
                .filter(catcherItem -> {
                    Duration duration = Duration.between(catcherItem.getUpdatedAt(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
                    long daysDiff = duration.toDays();
                    return catcherItem.getDeletedAt() == null && daysDiff >= 3;
                })
                .toList();

        List<CatcherItem> newItemList = new ArrayList<>();
        List<String> itemHashValueList = new ArrayList<>();

        for(MovieApiResponse.MovieItem response : (List<MovieApiResponse.MovieItem>)responses){
            if(response.getReleaseDate().isBlank() || response.getReleaseDate().isEmpty()) continue;

            String apiHashKey = response.getHashString();
            CatcherItem item = response.toEntity(null, category);

            if(itemHashKeyToDBItem.containsKey(apiHashKey)){
                itemHashValueList.add(item.getItemHashValue());
            }
            else{
                newItemList.add(item);
            }
        }

        if(!newItemList.isEmpty()){
            log.info("새로 추가된 아이템 개수: {}, 카테고리 = {}", newItemList.size(), category.getName());
            catcherItemRepository.saveAll(newItemList);
        }

        if(!itemHashValueList.isEmpty()){
            log.info("변경된 아이템 개수 : {}, 카테고리 = {}", itemHashValueList.size(), category.getName());
            catcherItemRepository.refreshUpdateDate(itemHashValueList);
        }

        if(!deleteList.isEmpty()){
            log.info("삭제된 아이템 개수 : {}, 카테고리 = {}", deleteList.size(), category.getName());
            catcherItemRepository.deleteAll(deleteList);
        }
    }
}
