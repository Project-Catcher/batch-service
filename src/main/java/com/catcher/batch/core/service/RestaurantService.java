package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.datasource.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        List<CatcherItem> catcherItems = restaurantApiResponse.getItems().stream()
                .filter(item -> !isDuplicateHashValue(hashString(item.getKey())))
                .map(item -> {
                    Location location = getLocation(item.getAddress());
                    String hashKey = hashString(item.getKey());

                    return CatcherItem.builder()
                            .category(category)
                            .location(location)
                            .title(item.getName())
                            .resourceUrl(item.getResourceUrl())
                            .itemHashValue(hashKey)
                            .build();
                })
                .collect(Collectors.toList());

        catcherItemRepository.saveAll(catcherItems);
    }

    private Location getLocation(String address) {
        String[] parts = address.split("\\s+");

        String province = parts[0];
        String city = parts[1];

        return locationRepository.findByDescription(province, city)
                .orElseThrow();
    }

    private boolean isDuplicateHashValue(String hashKey) {
        return catcherItemRepository.findByItemHashValue(hashKey).isPresent();
    }

    private String hashString(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
