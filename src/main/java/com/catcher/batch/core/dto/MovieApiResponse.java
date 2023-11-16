package com.catcher.batch.core.dto;

import com.catcher.batch.annotation.CatcherJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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

    @JsonProperty("results")
    private List<MovieItem> items;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class MovieItem {
        @JsonProperty("id")
        private int id;
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
    }

    @JsonProperty("total_pages")
    @Getter
    private int totalPages;

    @JsonProperty("total_results")
    @Getter
    private int totalResults;
}
