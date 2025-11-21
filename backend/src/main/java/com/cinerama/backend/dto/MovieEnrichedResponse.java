package com.cinerama.backend.dto;

import com.cinerama.backend.dto.tmdb.TmdbMovieDTO;
import com.cinerama.backend.enums.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Enriched Data Transfer Object that combines local Movie data with external TMDB data.
 * This DTO provides a unified view of movie information from both sources.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieEnrichedResponse {
    
    // ========== LOCAL DATA (from Cinerama database) ==========
    
    /**
     * Local movie ID from Cinerama database.
     */
    private Long localId;
    
    /**
     * Title from local database.
     */
    private String title;
    
    /**
     * Local short description for showtimes.
     */
    private String descriptionShowtimes;
    
    /**
     * Local complete movie description.
     */
    private String descriptionMovie;
    
    /**
     * Duration in minutes (local).
     */
    private Integer duration;
    
    /**
     * Local rating (PG, PG-13, R, etc.).
     */
    private String rating;
    
    /**
     * Local genre name.
     */
    private String genreName;
    
    /**
     * Local genre ID.
     */
    private Long genreId;
    
    /**
     * Local poster image URL.
     */
    private String imageUrl;
    
    /**
     * Local trailer URL.
     */
    private String trailerUrl;
    
    /**
     * Local director.
     */
    private String director;
    
    /**
     * Local cast list.
     */
    private List<String> cast;
    
    /**
     * Local release date.
     */
    private LocalDate releaseDate;
    
    /**
     * Local movie status (COMING_SOON, NOW_PLAYING, ENDED).
     */
    private MovieStatus status;
    
    /**
     * Timestamp when the local record was created.
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the local record was last updated.
     */
    private LocalDateTime updatedAt;
    
    // ========== EXTERNAL DATA (from TMDB) ==========
    
    /**
     * External TMDB movie ID.
     */
    private Long tmdbId;
    
    /**
     * TMDB movie overview/synopsis.
     */
    private String tmdbOverview;
    
    /**
     * TMDB poster URL (full path).
     */
    private String tmdbPosterUrl;
    
    /**
     * TMDB backdrop URL (full path).
     */
    private String tmdbBackdropUrl;
    
    /**
     * TMDB average vote/rating (0-10 scale).
     */
    private Double tmdbVoteAverage;
    
    /**
     * TMDB vote count.
     */
    private Integer tmdbVoteCount;
    
    /**
     * TMDB popularity score.
     */
    private Double tmdbPopularity;
    
    /**
     * TMDB original language.
     */
    private String tmdbOriginalLanguage;
    
    /**
     * TMDB runtime in minutes.
     */
    private Integer tmdbRuntime;
    
    /**
     * TMDB tagline.
     */
    private String tmdbTagline;
    
    /**
     * TMDB release date.
     */
    private String tmdbReleaseDate;
    
    /**
     * TMDB production status.
     */
    private String tmdbStatus;
    
    /**
     * TMDB homepage URL.
     */
    private String tmdbHomepage;
    
    /**
     * IMDB ID from TMDB.
     */
    private String imdbId;
    
    // ========== METADATA ==========
    
    /**
     * Indicates if local data is available.
     */
    private Boolean hasLocalData;
    
    /**
     * Indicates if TMDB data is available.
     */
    private Boolean hasTmdbData;
    
    /**
     * Indicates the data source priority used for display.
     */
    private String dataSource; // "LOCAL", "TMDB", "HYBRID"
    
    // ========== HELPER METHODS ==========
    
    /**
     * Creates an enriched response from local MovieResponse only.
     */
    public static MovieEnrichedResponse fromLocal(MovieResponse local) {
        return MovieEnrichedResponse.builder()
                .localId(local.getId())
                .title(local.getTitle())
                .descriptionShowtimes(local.getDescriptionShowtimes())
                .descriptionMovie(local.getDescriptionMovie())
                .duration(local.getDuration())
                .rating(local.getRating())
                .genreName(local.getGenreName())
                .genreId(local.getGenreId())
                .imageUrl(local.getImageUrl())
                .trailerUrl(local.getTrailerUrl())
                .director(local.getDirector())
                .cast(local.getCast())
                .releaseDate(local.getReleaseDate())
                .status(local.getStatus())
                .createdAt(local.getCreatedAt())
                .updatedAt(local.getUpdatedAt())
                .hasLocalData(true)
                .hasTmdbData(false)
                .dataSource("LOCAL")
                .build();
    }
    
    /**
     * Creates an enriched response from TMDB data only.
     */
    public static MovieEnrichedResponse fromTmdb(TmdbMovieDTO tmdb) {
        return MovieEnrichedResponse.builder()
                .tmdbId(tmdb.getTmdbId())
                .title(tmdb.getTitle())
                .tmdbOverview(tmdb.getOverview())
                .tmdbPosterUrl(tmdb.getFullPosterUrl())
                .tmdbBackdropUrl(tmdb.getFullBackdropUrl())
                .tmdbVoteAverage(tmdb.getVoteAverage())
                .tmdbVoteCount(tmdb.getVoteCount())
                .tmdbPopularity(tmdb.getPopularity())
                .tmdbOriginalLanguage(tmdb.getOriginalLanguage())
                .tmdbRuntime(tmdb.getRuntime())
                .tmdbTagline(tmdb.getTagline())
                .tmdbReleaseDate(tmdb.getReleaseDate())
                .tmdbStatus(tmdb.getStatus())
                .tmdbHomepage(tmdb.getHomepage())
                .imdbId(tmdb.getImdbId())
                .hasLocalData(false)
                .hasTmdbData(true)
                .dataSource("TMDB")
                .build();
    }
    
    /**
     * Creates an enriched response combining both local and TMDB data.
     */
    public static MovieEnrichedResponse fromHybrid(MovieResponse local, TmdbMovieDTO tmdb) {
        return MovieEnrichedResponse.builder()
                // Local data
                .localId(local.getId())
                .title(local.getTitle())
                .descriptionShowtimes(local.getDescriptionShowtimes())
                .descriptionMovie(local.getDescriptionMovie())
                .duration(local.getDuration())
                .rating(local.getRating())
                .genreName(local.getGenreName())
                .genreId(local.getGenreId())
                .imageUrl(local.getImageUrl())
                .trailerUrl(local.getTrailerUrl())
                .director(local.getDirector())
                .cast(local.getCast())
                .releaseDate(local.getReleaseDate())
                .status(local.getStatus())
                .createdAt(local.getCreatedAt())
                .updatedAt(local.getUpdatedAt())
                // TMDB data
                .tmdbId(tmdb.getTmdbId())
                .tmdbOverview(tmdb.getOverview())
                .tmdbPosterUrl(tmdb.getFullPosterUrl())
                .tmdbBackdropUrl(tmdb.getFullBackdropUrl())
                .tmdbVoteAverage(tmdb.getVoteAverage())
                .tmdbVoteCount(tmdb.getVoteCount())
                .tmdbPopularity(tmdb.getPopularity())
                .tmdbOriginalLanguage(tmdb.getOriginalLanguage())
                .tmdbRuntime(tmdb.getRuntime())
                .tmdbTagline(tmdb.getTagline())
                .tmdbReleaseDate(tmdb.getReleaseDate())
                .tmdbStatus(tmdb.getStatus())
                .tmdbHomepage(tmdb.getHomepage())
                .imdbId(tmdb.getImdbId())
                // Metadata
                .hasLocalData(true)
                .hasTmdbData(true)
                .dataSource("HYBRID")
                .build();
    }
}
