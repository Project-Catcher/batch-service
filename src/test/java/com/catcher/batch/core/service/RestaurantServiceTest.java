package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.datasource.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private CatcherItemRepository catcherItemRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @DisplayName("SUCCESS : 음식점 db 저장 성공 테스트")
    @Test
    void batchTest_SuccessfulSave() {
        // Given
        RestaurantApiResponse.RestaurantItem restaurantItem = createItem();

        RestaurantApiResponse restaurantApiResponse = RestaurantApiResponse.builder()
                .items(Collections.singletonList(restaurantItem))
                .build();

        Category category = Category.create("restaurant");
        Mockito.when(categoryRepository.findByName("restaurant")).thenReturn(Optional.of(category));
        Mockito.when(locationRepository.findByDescription("서울", "관악구")).thenReturn(Optional.of(Location.create("1162000000", "37.4783683761333", "126.951561853868", "서울 관악구")));
        Mockito.when(catcherItemRepository.findByItemHashValue(Mockito.anyString())).thenReturn(Optional.empty());

        // When
        restaurantService.batch(restaurantApiResponse);

        // Then
        Mockito.verify(catcherItemRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @DisplayName("SUCCESS: 음식점 db 저장 성공 테스트 - 중복 객체 확인")
    @Test
    void batchTest_SuccessfulSaveWithDuplicate() {
        // Given
        RestaurantApiResponse.RestaurantItem restaurantItem = createItem();

        RestaurantApiResponse restaurantApiResponse = RestaurantApiResponse.builder()
                .items(Arrays.asList(restaurantItem, restaurantItem))
                .build();

        Category category = Category.create("restaurant");
        Mockito.when(categoryRepository.findByName("restaurant")).thenReturn(Optional.of(category));
        Mockito.when(locationRepository.findByDescription("서울", "관악구")).thenReturn(Optional.of(Location.create("1162000000", "37.4783683761333", "126.951561853868", "서울 관악구")));
        Mockito.when(catcherItemRepository.findByItemHashValue(Mockito.anyString())).thenReturn(Optional.empty());

        // When
        restaurantService.batch(restaurantApiResponse);

        // Then
        Mockito.verify(catcherItemRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    private RestaurantApiResponse.RestaurantItem createItem() {
        return RestaurantApiResponse.RestaurantItem.builder()
                .key("key")
                .address("서울 관악구")
                .name("맛집")
                .resourceUrl("url")
                .build();
    }
}