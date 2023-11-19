package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.catcher.batch.common.utils.Hash.hashGenerator;
import static com.catcher.batch.common.utils.Hash.isUpdated;
import static com.catcher.batch.common.utils.HashCodeGenerator.hashString;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    public static final String CATEGORY_NAME = "restaurant";

    @Transactional
    public void batch(RestaurantApiResponse restaurantApiResponse) {
        Category category = categoryRepository.findByName(CATEGORY_NAME)
                .orElseGet(() -> categoryRepository.save(Category.create(CATEGORY_NAME)));

        Map<String, CatcherItem> itemMap = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, Function.identity()));

        List<CatcherItem> catcherItems = new ArrayList<>();
        List<RestaurantApiResponse.RestaurantItem> restaurantItems = restaurantApiResponse.getItems().getItem();
        for (RestaurantApiResponse.RestaurantItem restaurantItem : restaurantItems) {
            String hashKey = hashString(CATEGORY_NAME, restaurantItem.getKey());
            if (itemMap.containsKey(hashKey)) {
                CatcherItem savedItem = itemMap.get(hashKey);
                int itemHash = hashGenerator(savedItem.getTitle(), savedItem.getThumbnailUrl());
                int responseHash = hashGenerator(restaurantItem.getName(), restaurantItem.getThumbnailUrl());

                if (isUpdated(itemHash, responseHash)) {
                    CatcherItem changedItem = createItem(hashKey, restaurantItem, category);
                    savedItem.changeContents(changedItem);

                    itemMap.put(hashKey, savedItem);
                    catcherItems.add(savedItem);
                }
                continue;
            }

            CatcherItem newItem = createItem(hashKey, restaurantItem, category);
            itemMap.put(hashKey, newItem);
            catcherItems.add(newItem);
        }

        if (!catcherItems.isEmpty()) {
            catcherItemRepository.saveAll(catcherItems);
        }
    }


    private Location getLocation(String address) {
        String[] parts = address.split("\\s+");

        String province = parts[0];
        String city = parts[1];
        String withoutDo = province.replace("도", "");

        return locationRepository.findByDescription(withoutDo, city)
                .orElseThrow();
    }

    private CatcherItem createItem(
            String hashKey, RestaurantApiResponse.RestaurantItem restaurantItem, Category category
    ) {
        Location location = getLocation(restaurantItem.getAddress());

        return CatcherItem.builder()
                .category(category)
                .location(location)
                .title(restaurantItem.getName())
                .thumbnailUrl(restaurantItem.getThumbnailUrl())
                .itemHashValue(hashKey)
                .build();
    }
}
