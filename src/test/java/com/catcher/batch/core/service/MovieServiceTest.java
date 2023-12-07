package com.catcher.batch.core.service;

import com.catcher.batch.common.utils.HashCodeGenerator;
import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.database.CategoryRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.catcher.batch.core.dto.MovieApiResponse;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@Transactional
class MovieServiceTest {
    private final String CATEGORY = "movie";

    @Mock
    private CatcherItemRepository catcherItemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MovieService movieService;

    private Location location;

    private Category category;

    @BeforeEach
    void beforeEach() {
        location = Mockito.mock(Location.class);
        category = Category.create(CATEGORY);
    }

    @DisplayName("API의 응답으로 0개가 전달됐다면, DB에 어떠한 작업도 안일어나야 한다")
    @Test
    void having_zero_items_does_nothing() {
        //given
        List<MovieApiResponse.MovieItem> itemList = new ArrayList<>();

        //when
        movieService.batch(itemList);

        //then
        Mockito.verify(catcherItemRepository, Mockito.never()).deleteAll(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.never()).saveAll(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.never()).refreshUpdateDate(Mockito.anyList());
    }

    @DisplayName("영화 아이템을 처음 적재한다면 saveAll 함수만 실행되어야 한다")
    @Test
    void first_saving_movie_item() {
        //given
        basicSetting();
        List<MovieApiResponse.MovieItem> itemList = new ArrayList<>();
        List<CatcherItem> catcherItems = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            MovieApiResponse.MovieItem item = generateMovieItem("영화 제목 " + i, generateRandomString(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
            itemList.add(item);
            catcherItems.add(item.toEntity(location, category));
        }

        //when
        movieService.batch(itemList);

        //then
        Mockito.verify(catcherItemRepository, Mockito.never()).deleteAll(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.never()).refreshUpdateDate(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.atLeast(1)).saveAll(catcherItems);
    }

    @DisplayName("DB의 영화 데이터와 API를 통해 불러온 영화 데이터가 일부만 겹친다면 새로운것은 save, 겹치는것은 update되어야 한다")
    @Test
    void batch_requested_after_first_having_some_new_and_dup() {
        //given
        basicSetting();
        List<MovieApiResponse.MovieItem> dbItemList = new ArrayList<>();
        List<MovieApiResponse.MovieItem> newItemList = new ArrayList<>();
        List<CatcherItem> catcherItems = new ArrayList<>();
        List<CatcherItem> newCatcherItems = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            MovieApiResponse.MovieItem item = generateMovieItem("영화 제목 " + i, generateRandomString(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
            dbItemList.add(item);
            newItemList.add(item);
            catcherItems.add(item.toEntity(location, category));
        }
        Mockito.when(catcherItemRepository.findByCategory(category)).thenReturn(catcherItems);

        for (int i = 6; i <= 10; i++) {
            MovieApiResponse.MovieItem item = generateMovieItem("영화 제목 " + i, generateRandomString(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
            newItemList.add(item);
            newCatcherItems.add(item.toEntity(location, category));
        }

        //when
        movieService.batch(newItemList);
        //then
        Mockito.verify(catcherItemRepository, Mockito.never()).deleteAll(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.atLeast(1)).refreshUpdateDate(
                catcherItems.stream()
                .map(CatcherItem::getItemHashValue)
                .toList()
        );
        Mockito.verify(catcherItemRepository, Mockito.atLeast(1)).saveAll(newCatcherItems);
    }

    @DisplayName("업데이트된지 3일이 지난 영화는 삭제처리 로직을 수행한다")
    @Test
    void diff_between_today_and_recent_update_date_over_3days_will_be_deleted(){
        //given
        basicSetting();
        List<MovieApiResponse.MovieItem> itemList = new ArrayList<>();
        List<MovieApiResponse.MovieItem> newItemList = new ArrayList<>();
        List<CatcherItem> catcherItems = new ArrayList<>();
        List<CatcherItem> newCatcherItems = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            MovieApiResponse.MovieItem item = generateMovieItem("영화 제목 " + i, generateRandomString(), ZonedDateTime.now().minusDays(5));
            itemList.add(item);
            catcherItems.add(item.toEntity(location, category));
        }
        Mockito.when(catcherItemRepository.findByCategory(category)).thenReturn(catcherItems);

        MovieApiResponse.MovieItem newItem = generateMovieItem("영화 제목 " + 6, generateRandomString(), ZonedDateTime.now());
        newItemList.add(newItem);
        newCatcherItems.add(newItem.toEntity(location, category));

        //when
        movieService.batch(newItemList);

        //then
        Mockito.verify(catcherItemRepository, Mockito.atLeast(1)).deleteAll(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.never()).refreshUpdateDate(Mockito.anyList());
        Mockito.verify(catcherItemRepository, Mockito.atLeast(1)).saveAll(Mockito.anyList());
    }

    private MovieApiResponse.MovieItem generateMovieItem(String title, String path, ZonedDateTime updatedAt) {
        MovieApiResponse.MovieItem movieItem = mock(MovieApiResponse.MovieItem.class);
        String hashKey = HashCodeGenerator.hashString(CATEGORY, generateRandomInt());

        when(movieItem.getCategory()).thenReturn(CATEGORY);
        when(movieItem.getId()).thenReturn(generateRandomInt());
        when(movieItem.getHashString()).thenReturn(hashKey);
        when(movieItem.getTitle()).thenReturn(title);
        when(movieItem.getPosterPath()).thenReturn(path);
        when(movieItem.getReleaseDate()).thenReturn(String.valueOf(updatedAt));

        CatcherItem catcherItem = mock(CatcherItem.class);
        when(catcherItem.getTitle()).thenReturn(title);
        when(catcherItem.getItemHashValue()).thenReturn(hashKey);
        when(catcherItem.getThumbnailUrl()).thenReturn(path);
        when(catcherItem.getStartAt()).thenReturn(updatedAt);
        when(catcherItem.getCategory()).thenReturn(category);
        when(catcherItem.getUpdatedAt()).thenReturn(updatedAt);

        when(movieItem.toEntity(Mockito.any(), Mockito.any())).thenReturn(catcherItem);

        return movieItem;
    }

    private void basicSetting() {
        Mockito.when(categoryRepository.findByName(CATEGORY)).thenReturn(Optional.of(category));
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }
}