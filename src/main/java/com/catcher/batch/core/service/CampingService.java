package com.catcher.batch.core.service;

import com.catcher.batch.core.port.AddressPort;
import com.catcher.batch.core.port.CatcherItemRepository;
import com.catcher.batch.core.port.CategoryRepository;
import com.catcher.batch.core.port.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.CampingApiResponse;
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
    private final AddressPort addressPort;
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
                    Location location = getLocation(campingItem.getAddress());
                    String hashKey = hashString(CATEGORY_NAME, campingItem.getKey());

                    itemMap.put(hashKey, campingItem.getName());

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

        if (!catcherItems.isEmpty()) {
            catcherItemRepository.saveAll(catcherItems);
        }
    }

    private Location getLocation(String address) {
        final String areaCode =  addressPort.getAreaCodeByQuery(address).orElseThrow();
        return locationRepository.findByAreaCode(areaCode).orElseThrow();
    }
}
