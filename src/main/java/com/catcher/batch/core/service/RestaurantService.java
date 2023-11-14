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

import java.util.Map;
import java.util.stream.Collectors;

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

        Map<String, String> itemMap = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, CatcherItem::getTitle));

        restaurantApiResponse.getItems().stream()
                .filter(item -> !itemMap.containsKey(hashString(CATEGORY_NAME, item.getKey())))
                .forEach(item -> {
                    Location location = getLocation(item.getAddress());
                    String hashKey = hashString(CATEGORY_NAME, item.getKey());

                    CatcherItem catcherItem = CatcherItem.builder()
                            .category(category)
                            .location(location)
                            .title(item.getName())
                            .resourceUrl(item.getResourceUrl())
                            .itemHashValue(hashKey)
                            .build();

                    catcherItemRepository.save(catcherItem);
                });
    }

    private Location getLocation(String address) {
        String[] parts = address.split("\\s+");

        String province = parts[0];
        String city = parts[1];

        return locationRepository.findByDescription(province, city)
                .orElseThrow();
    }
}
