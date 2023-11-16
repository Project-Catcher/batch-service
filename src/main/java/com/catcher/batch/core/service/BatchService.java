package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BatchService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    public void batch(List<? extends ApiResponse> apiResponses) {
        Category category = categoryRepository.findByName(apiResponses.get(0).getCategory())
                .orElseGet(() -> categoryRepository.save(Category.create(apiResponses.get(0).getCategory())));

        Map<String, CatcherItem> itemMap = catcherItemRepository.findByCategory(category).stream()
                .collect(Collectors.toMap(CatcherItem::getItemHashValue, Function.identity()));

        List<CatcherItem> deleteItems = new ArrayList<>();
        List<CatcherItem> saveItems = new ArrayList<>();

        List<CatcherItem> catcherItems = apiResponses.stream()
                .filter(apiResponse -> {
                    String hashKey = hashString(apiResponse);
                    if (itemMap.containsKey(hashKey)) {
                        if (isExpired(apiResponse.getEndAt())) {
                            deleteItems.add(itemMap.get(hashKey));
                        }
                        if (isContentChanged(itemMap.get(hashKey), apiResponse)) {
                            CatcherItem e = itemMap.get(hashKey);
                            // e.changeContent(dsfasfaf,dadasd); ... 요기에 바꾸는 로직 추가
                            saveItems.add(e);
                        }
                        return false;
                    } else {
                        if (isExpired(apiResponse.getEndAt())) {
                            return false;
                        }
                        return true;
                    }
                })
                .map(apiResponse -> apiResponseToCatcherItem(apiResponse, category, getLocation(apiResponse)))
                .toList();

        if (!saveItems.isEmpty()) {
            catcherItemRepository.saveAll(saveItems);
            catcherItemRepository.saveAll(catcherItems);
        }

        if (!deleteItems.isEmpty()) {
            catcherItemRepository.deleteAll(deleteItems);
        }
    }

    protected abstract String hashString(ApiResponse apiResponse);

    protected abstract boolean isContentChanged(CatcherItem catcherItem, ApiResponse apiResponse);

    protected abstract Location getLocation(ApiResponse apiResponse);

    protected Location getLocation(String province, String city) {
        String withoutDo = province.replace("도", "");

        return locationRepository.findByDescription(withoutDo, city)
                .orElseThrow();
    }

    protected Location getLocation(String address) {
        String[] parts = address.split("\\s+");

        String province = parts[0];
        String city = parts[1];

        return locationRepository.findByDescription(province, city)
                .orElseThrow();
    }

    protected CatcherItem apiResponseToCatcherItem(ApiResponse apiResponse, Category category, Location location) {
        return CatcherItem.builder()
                .category(category)
                .thumbnailUrl(apiResponse.getThumbnailUrl())
                .location(location)
                .itemHashValue(hashString(apiResponse))
                .description(apiResponse.getDescription())
                .resourceUrl(apiResponse.getResourceUrl())
                .endAt(apiResponse.getEndAt())
                .startAt(apiResponse.getStartAt())
                .build();
    }

    private boolean isExpired(ZonedDateTime endDateTime) {
        return endDateTime != null && ZonedDateTime.now().isAfter(endDateTime);
    }
}
