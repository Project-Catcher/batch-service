package com.catcher.batch.core.service;

import com.catcher.batch.core.port.AddressPort;
import com.catcher.batch.core.port.CatcherItemRepository;
import com.catcher.batch.core.port.CategoryRepository;
import com.catcher.batch.core.port.LocationRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import org.apache.commons.codec.digest.DigestUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private CatcherItemRepository catcherItemRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AddressPort addressPort;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantApiResponse restaurantApiResponse;
    private RestaurantApiResponse.RestaurantItem restaurantItem;
    private CatcherItem catcherItem;
    private Location location;
    private Category category;

    @BeforeEach
    void beforeEach() {
        restaurantApiResponse = Mockito.mock(RestaurantApiResponse.class);
        restaurantItem = Mockito.mock(RestaurantApiResponse.RestaurantItem.class);
        catcherItem = Mockito.mock(CatcherItem.class);
        location = Mockito.mock(Location.class);
        category = Mockito.mock(Category.class);
    }

    @DisplayName("SUCCESS : 음식점 db 저장 성공 테스트")
    @Test
    void batchTest_SuccessfulSave() {
        // Given
        setUpRestaurantItem();

        // When
        restaurantService.batch(restaurantApiResponse);

        // Then
        Mockito.verify(catcherItemRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @DisplayName("SUCCESS: 음식점 db 저장 성공 테스트 - 중복 객체 확인")
    @Test
    void batchTest_SuccessfulSaveWithDuplicate() {
        // Given
        String hashKey = DigestUtils.sha256Hex("restaurant" + "-" + "key");

        when(catcherItem.getItemHashValue()).thenReturn(hashKey);
        when(catcherItem.getLocation()).thenReturn(location);
        when(catcherItem.getTitle()).thenReturn("저장된 맛집");
        when(catcherItem.getCategory()).thenReturn(category);

        when(catcherItemRepository.findByCategory(any(Category.class))).thenAnswer(invocation -> Collections.singletonList(catcherItem));

        setUpRestaurantItem();

        // When
        restaurantService.batch(restaurantApiResponse);

        // Then
        Mockito.verify(catcherItemRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    private void setUpRestaurantItem() {
        when(restaurantItem.getKey()).thenReturn("key");
        when(restaurantItem.getName()).thenReturn("맛집");
        when(restaurantItem.getResourceUrl()).thenReturn("url");
        when(restaurantItem.getAddress()).thenReturn("서울 관악구");
        when(restaurantItem.getLatitude()).thenReturn("37.4783683761333");
        when(restaurantItem.getLongitude()).thenReturn("126.951561853868");

        Mockito.when(restaurantApiResponse.getItems()).thenReturn(List.of(restaurantItem));
        Mockito.when(categoryRepository.findByName("restaurant")).thenReturn(Optional.of(category));
        Mockito.when(addressPort.getAreaCodeByQuery("서울 관악구")).thenReturn(Optional.of("1000"));
        Mockito.when(locationRepository.findByAreaCode("1000")).thenReturn(Optional.of(location));
    }
}