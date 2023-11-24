package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.ApiResponse;
import com.catcher.batch.core.port.AddressPort;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CampingService extends BatchService {

    public CampingService(
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
        int responseHash = Objects.hash(newCatcherItem.getTitle(), newCatcherItem.getThumbnailUrl(), newCatcherItem.getResourceUrl(), newCatcherItem.getDescription());
        int catcherHash = Objects.hash(originCatcherItem.getTitle(), originCatcherItem.getThumbnailUrl(), originCatcherItem.getResourceUrl(), originCatcherItem.getDescription());
        return responseHash != catcherHash;
    }
}
