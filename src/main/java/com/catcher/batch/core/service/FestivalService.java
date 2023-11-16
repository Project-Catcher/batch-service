package com.catcher.batch.core.service;

import com.catcher.batch.common.utils.HashCodeGenerator;
import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FestivalService extends BatchService {

    public FestivalService(CatcherItemRepository catcherItemRepository, CategoryRepository categoryRepository, LocationRepository locationRepository) {
        super(catcherItemRepository, categoryRepository, locationRepository);
    }

    @Override
    protected boolean isContentChanged(CatcherItem catcherItem, ApiResponse apiResponse) {
        int responseHash = Objects.hash(catcherItem.getStartAt(), catcherItem.getEndAt(), catcherItem.getTitle());
        int catcherHash = Objects.hash(apiResponse.getStartAt(), apiResponse.getEndAt(), apiResponse.getTitle());
        return responseHash != catcherHash;
    }

    @Override
    protected String hashString(ApiResponse apiResponse) {
        return HashCodeGenerator.hashString(apiResponse.getCategory(), apiResponse.getTitle());
    }

    @Override
    protected Location getLocation(ApiResponse apiResponse) {
        return getLocation(apiResponse.getAddress());
    }
}
