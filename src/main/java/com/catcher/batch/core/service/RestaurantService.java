package com.catcher.batch.core.service;

import com.catcher.batch.core.port.AddressPort;
import com.catcher.batch.core.port.CatcherItemRepository;
import com.catcher.batch.core.port.CategoryRepository;
import com.catcher.batch.core.port.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.catcher.batch.common.utils.HashCodeGenerator.hashString;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    private final AddressPort addressPort;
    private final LocationRepository locationRepository;
    public static final String CATEGORY_NAME = "restaurant";

    @Transactional
    public void batch(RestaurantApiResponse restaurantApiResponse) {
        Category category = categoryRepository.findByName(CATEGORY_NAME)
                .orElseGet(() -> categoryRepository.save(Category.create(CATEGORY_NAME)));

        Map<String, String> itemMap = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, CatcherItem::getTitle));

        List<CatcherItem> catcherItems = restaurantApiResponse.getItems().stream()
                .filter(item -> !itemMap.containsKey(hashString(CATEGORY_NAME, item.getKey())))
                .map(item -> {
                    Location location = getLocation(item.getAddress());
                    String hashKey = hashString(CATEGORY_NAME, item.getKey());

                    itemMap.put(hashKey, item.getName());

                    return CatcherItem.builder()
                            .category(category)
                            .location(location)
                            .title(item.getName())
                            .resourceUrl(item.getResourceUrl())
                            .itemHashValue(hashKey)
                            .build();
                })
                .collect(Collectors.toList());

        if (!catcherItems.isEmpty()) {
            catcherItemRepository.saveAll(catcherItems);
        }
    }

    private Location getLocation(String address) {
        final String areaCode =  addressPort.getAreaCodeByQuery(address).orElseThrow();
        return locationRepository.findByAreaCode(areaCode).orElseThrow();
    }
}
