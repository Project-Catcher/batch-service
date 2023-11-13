package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.LocationRepository;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.datasource.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
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

    private RestaurantApiResponse restaurantApiResponse;
    private RestaurantApiResponse.RestaurantItem restaurantItem;

    @BeforeEach
    void beforeEach() {
        restaurantApiResponse = mock(RestaurantApiResponse.class);
        restaurantItem = Mockito.mock(RestaurantApiResponse.RestaurantItem.class);
    }

    @DisplayName("SUCCESS : 음식점 db 저장 성공 테스트")
    @Test
    void batchTest_SuccessfulSave() {
        // Given
        setUpRestaurantItem();
        when(restaurantApiResponse.getItems()).thenReturn(List.of(restaurantItem));

        // When
        restaurantService.batch(restaurantApiResponse);

        // Then
        Mockito.verify(catcherItemRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @DisplayName("SUCCESS: 음식점 db 저장 성공 테스트 - 중복 객체 확인")
    @Test
    void batchTest_SuccessfulSaveWithDuplicate() {
        // Given
        setUpRestaurantItem();
        when(restaurantApiResponse.getItems()).thenReturn(Arrays.asList(restaurantItem, restaurantItem));

        // When
        restaurantService.batch(restaurantApiResponse);

        // Then
        Mockito.verify(catcherItemRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    private void setUpRestaurantItem() {
        when(restaurantItem.getKey()).thenReturn("key");
        when(restaurantItem.getName()).thenReturn("맛집");
        when(restaurantItem.getResourceUrl()).thenReturn("url");
        when(restaurantItem.getAddress()).thenReturn("서울 관악구");
        when(restaurantItem.getLatitude()).thenReturn("37.4783683761333");
        when(restaurantItem.getLongitude()).thenReturn("126.951561853868");

        when(restaurantApiResponse.getItems()).thenReturn(List.of(restaurantItem));
        Mockito.when(categoryRepository.findByName("restaurant")).thenReturn(Optional.of(Category.create("restaurant")));
        Mockito.when(locationRepository.findByDescription("서울", "관악구")).thenReturn(Optional.of(Location.create("1162000000", "37.4783683761333", "126.951561853868", "서울 관악구")));
    }
}