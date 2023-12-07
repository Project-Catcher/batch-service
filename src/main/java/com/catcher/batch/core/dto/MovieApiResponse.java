package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.catcher.batch.common.utils.HashCodeGenerator;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.core.domain.entity.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@CatcherJson(path = "")
public class MovieApiResponse {

    @JsonProperty("dates")
    private MovieNowPlayingDate movieNowPlayingDate;

    @Getter
    private static class MovieNowPlayingDate {
        @JsonProperty("maximum")
        private String maximumDate;

        @JsonProperty("minimum")
        private String minimumDate;
    }

    @JsonProperty("total_pages")
    @Getter
    private Integer totalPages;

    @JsonProperty("total_results")
    @Getter
    private Integer totalResults;

    @JsonProperty("results")
    private List<MovieItem> items;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class MovieItem implements ApiResponse {
        private final static String CATEGORY = "movie";

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("original_title")
        private String originalTitle;
        @JsonProperty("poster_path")
        private String posterPath;
        @JsonProperty("release_date")
        private String releaseDate;
        @JsonProperty("title")
        private String title;
        @JsonProperty("overview")
        private String overview;

        @JsonProperty("poster_path")
        public void setPosterPath(String posterPath) {
            this.posterPath = "https://image.tmdb.org/t/p/w300" + posterPath;
        }

        @JsonProperty("title")
        public void setTitle(String title){
            String removeEmojiRegex = "[\\x{1F600}-\\x{1F64F}\\x{1F300}-\\x{1F5FF}\\x{1F680}-\\x{1F6FF}\\x{1F700}-\\x{1F77F}\\x{1F780}-\\x{1F7FF}\\x{1F800}-\\x{1F8FF}\\x{1F900}-\\x{1F9FF}\\x{1FA00}-\\x{1FA6F}\\x{1FA70}-\\x{1FAFF}]";
            this.title = title.replaceAll(removeEmojiRegex,"");
        }

        @Override
        public ZonedDateTime getEndAt() {
            return null;
        }

        @Override
        public String getAddress() {
            return null;
        }

        @Override
        public String getHashString() {
            return HashCodeGenerator.hashString(getCategory(), String.valueOf(getId()));
        }

        @Override
        public String getCategory() {
            return CATEGORY;
        }

        @Override
        public CatcherItem toEntity(Location location, Category category) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate localDate = LocalDate.parse(releaseDate, dateFormatter);
            ZonedDateTime startAt = localDate.atStartOfDay(zoneId);

            return CatcherItem
                    .builder()
                    .title(title)
                    .itemHashValue(getHashString())
                    .thumbnailUrl(posterPath)
                    .startAt(startAt)
                    .category(category)
                    .build();
        }
    }
}
