package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.datasource.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.catcher.batch.common.utils.HashCodeGenerator.hashString;

@Service
@RequiredArgsConstructor
public class CampingService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    public static final String CATEGORY_NAME = "camping";

    @Transactional
    public void batch(CampingApiResponse campingApiResponse) {
        Category category = categoryRepository.findByName(CATEGORY_NAME)
                .orElseGet(() -> categoryRepository.save(Category.create(CATEGORY_NAME)));

        Map<String, String> itemMap = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, CatcherItem::getTitle));

        List<CampingApiResponse.CampingItem> campingItems = campingApiResponse.getItems().getItem();

        List<CatcherItem> catcherItems = campingItems.stream()
                .filter(campingItem -> !itemMap.containsKey(hashString(CATEGORY_NAME, campingItem.getKey())))
                .map(campingItem -> {
                    Location location = getLocationByDescription(campingItem.getProvince(), campingItem.getCity());
                    String hashKey = hashString(CATEGORY_NAME, campingItem.getKey());

                    return CatcherItem.builder()
                            .category(category)
                            .location(location)
                            .title(campingItem.getName())
                            .description(campingItem.getDescription())
                            .thumbnailUrl(campingItem.getThumbnailUrl())
                            .itemHashValue(hashKey)
                            .build();
                })
                .collect(Collectors.toList());

        catcherItemRepository.saveAll(catcherItems);
    }

    private Location getLocationByDescription(String province, String city) {
        String withoutDo = province.replace("도", "");

        return locationRepository.findByDescription(withoutDo, city)
                .orElseThrow();
    }
}
