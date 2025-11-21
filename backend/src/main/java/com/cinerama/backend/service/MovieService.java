package com.cinerama.backend.service;

import com.cinerama.backend.dto.MovieEnrichedResponse;
import com.cinerama.backend.dto.MovieRequest;
import com.cinerama.backend.dto.MovieResponse;
import com.cinerama.backend.dto.tmdb.TmdbSearchResultDTO;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieResponse> getAllMovies();
    Optional<MovieResponse> getMovieById(Long id);
    MovieResponse saveMovie(MovieRequest movieRequest);
    MovieResponse updateMovie(Long id, MovieRequest movieRequest);
    void deleteMovie(Long id);
    void updateMovieStatuses();
    
    // ========== NEW METHODS FOR TMDB INTEGRATION ==========
    
    /**
     * Gets enriched movie data combining local and TMDB information.
     * If movie exists locally, it enriches with TMDB data.
     * If not, returns TMDB data only.
     * 
     * @param localId Local movie ID from Cinerama database
     * @param tmdbId Optional TMDB ID for fetching external data
     * @return Optional containing enriched movie data
     */
    Optional<MovieEnrichedResponse> getEnrichedMovieById(Long localId, Long tmdbId);
    
    /**
     * Gets all movies enriched with TMDB data where available.
     * 
     * @return List of enriched movie responses
     */
    List<MovieEnrichedResponse> getAllEnrichedMovies();
    
    /**
     * Searches movies from TMDB external API.
     * 
     * @param query Search query string
     * @param page Page number for pagination
     * @return Search results from TMDB
     */
    TmdbSearchResultDTO searchMoviesFromTmdb(String query, Integer page);
    
    /**
     * Gets popular movies from TMDB.
     * 
     * @param page Page number for pagination
     * @return Popular movies from TMDB
     */
    TmdbSearchResultDTO getPopularMoviesFromTmdb(Integer page);
    
    /**
     * Gets now playing movies from TMDB.
     * 
     * @param page Page number for pagination
     * @return Now playing movies from TMDB
     */
    TmdbSearchResultDTO getNowPlayingMoviesFromTmdb(Integer page);
    
    /**
     * Gets upcoming movies from TMDB.
     * 
     * @param page Page number for pagination
     * @return Upcoming movies from TMDB
     */
    TmdbSearchResultDTO getUpcomingMoviesFromTmdb(Integer page);
}