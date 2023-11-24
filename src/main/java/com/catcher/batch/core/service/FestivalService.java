package com.catcher.batch.core.service;

import com.catcher.batch.common.utils.HashCodeGenerator;
import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.ApiResponse;
import com.catcher.batch.core.port.AddressPort;
import io.micrometer.common.util.StringUtils;

import java.util.Objects;

public class FestivalService extends BatchService {

    public FestivalService(CatcherItemRepository catcherItemRepository, CategoryRepository categoryRepository, LocationRepository locationRepository, AddressPort addressPort) {
        super(catcherItemRepository, categoryRepository, locationRepository, addressPort);
    }

    @Override
    protected boolean isContentChanged(CatcherItem originCatcherItem, CatcherItem newCatcherItem) {
        int responseHash = Objects.hash(newCatcherItem.getStartAt(), newCatcherItem.getEndAt(), newCatcherItem.getTitle());
        int catcherHash = Objects.hash(originCatcherItem.getStartAt(), originCatcherItem.getEndAt(), originCatcherItem.getTitle());
        return responseHash != catcherHash;
    }

    @Override
    protected String hashString(ApiResponse apiResponse) {
        return HashCodeGenerator.hashString(apiResponse.getHashString());
    }

    @Override
    protected Location getLocation(ApiResponse apiResponse) {
        if (!StringUtils.isBlank(apiResponse.getAddress())) {
            return getLocation(apiResponse.getAddress());
        }
        return null;
    }
}
