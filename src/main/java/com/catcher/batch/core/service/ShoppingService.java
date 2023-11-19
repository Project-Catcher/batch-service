package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.ShoppingApiResponse;
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
public class ShoppingService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    public static final String CATEGORY_NAME = "shopping";

    @Transactional
    public void batch(ShoppingApiResponse shoppingApiResponse) {
        Category category = categoryRepository.findByName(CATEGORY_NAME)
                .orElseGet(() -> categoryRepository.save(Category.create(CATEGORY_NAME)));

        Map<String, CatcherItem> itemMap = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, Function.identity()));

        List<CatcherItem> catcherItems = new ArrayList<>();
        List<ShoppingApiResponse.ShoppingItem> shoppingItems = shoppingApiResponse.getItems().getItem();
        for (ShoppingApiResponse.ShoppingItem shoppingItem : shoppingItems) {
            String hashKey = hashString(CATEGORY_NAME, shoppingItem.getKey());
            if (itemMap.containsKey(hashKey)) {
                CatcherItem savedItem = itemMap.get(hashKey);
                int itemHash = hashGenerator(savedItem.getTitle(), savedItem.getThumbnailUrl());
                int responseHash = hashGenerator(shoppingItem.getName(), shoppingItem.getThumbnailUrl());

                if (isUpdated(itemHash, responseHash)) {
                    CatcherItem changedItem = createItem(hashKey, shoppingItem, category);
                    savedItem.changeContents(changedItem);

                    itemMap.put(hashKey, savedItem);
                    catcherItems.add(savedItem);
                }
                continue;
            }

            CatcherItem newItem = createItem(hashKey, shoppingItem, category);
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

        return locationRepository.findByDescription(province, city)
                .orElseThrow();
    }

    private CatcherItem createItem(
            String hashKey, ShoppingApiResponse.ShoppingItem shoppingItem, Category category
    ) {
        Location location = getLocation(shoppingItem.getAddress());

        return CatcherItem.builder()
                .category(category)
                .location(location)
                .title(shoppingItem.getName())
                .thumbnailUrl(shoppingItem.getThumbnailUrl())
                .itemHashValue(hashKey)
                .build();
    }
}
