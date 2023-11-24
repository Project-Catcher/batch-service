package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.ApiResponse;
import com.catcher.batch.core.port.AddressPort;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RestaurantService extends BatchService {

    public RestaurantService(
            CatcherItemRepository catcherItemRepository,
            CategoryRepository categoryRepository,
            LocationRepository locationRepository,
            AddressPort addressPort
    ) {
        super(catcherItemRepository, categoryRepository, locationRepository, addressPort);
    }

    @Override
    protected Location getLocation(ApiResponse apiResponse) {
        if (!StringUtils.isBlank(apiResponse.getAddress())) {
            return getLocation(apiResponse.getAddress());
        }
        return null;
    }

    @Override
    protected boolean isContentChanged(CatcherItem originCatcherItem, CatcherItem newCatcherItem) {
        int responseHash = Objects.hash(newCatcherItem.getTitle(), newCatcherItem.getThumbnailUrl());
        int catcherHash = Objects.hash(originCatcherItem.getTitle(), newCatcherItem.getThumbnailUrl());
        return responseHash != catcherHash;
    }
}
