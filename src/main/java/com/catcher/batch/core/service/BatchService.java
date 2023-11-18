package com.catcher.batch.core.service;

import com.catcher.batch.common.utils.HashCodeGenerator;
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

import static com.catcher.batch.utils.CustomBeanUtils.*;

@RequiredArgsConstructor
public abstract class BatchService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Transactional
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
                    CatcherItem savedCatcherItem = null;

                    if ((savedCatcherItem = itemMap.get(hashKey)) != null) {
                        if (isExpired(apiResponse.getEndAt())) {
                            deleteItems.add(savedCatcherItem);
                        }
                        CatcherItem receivedCatcherItem = apiResponse.toEntity(getLocation(apiResponse));

                        if (isContentChanged(savedCatcherItem, receivedCatcherItem)) {
                            copyProperties(receivedCatcherItem, savedCatcherItem);
                            saveItems.add(savedCatcherItem);
                        }

                        return false;
                    }
                    return !isExpired(apiResponse.getEndAt());
                })
                .map(apiResponse -> apiResponse.toEntity(getLocation(apiResponse)))
                .toList();

        if (!saveItems.isEmpty()) {
            catcherItemRepository.saveAll(saveItems);
        }

        if (!catcherItems.isEmpty()) {
            catcherItemRepository.saveAll(catcherItems);
        }

        if (!deleteItems.isEmpty()) {
            catcherItemRepository.deleteAll(deleteItems);
        }
    }

    protected abstract Location getLocation(ApiResponse apiResponse);

    protected boolean isContentChanged(CatcherItem originCatcherItem, CatcherItem newCatcherItem) {
        return false;
    }

    protected String hashString(ApiResponse apiResponse) {
        return HashCodeGenerator.hashString(apiResponse.getHashString());
    }

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

    private boolean isExpired(ZonedDateTime endDateTime) {
        return endDateTime != null && ZonedDateTime.now(ApiResponse.zoneId).isAfter(endDateTime);
    }
}
