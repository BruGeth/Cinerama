package com.cinerama.backend.service;

import com.cinerama.backend.dto.tmdb.TmdbMovieDTO;
import com.cinerama.backend.dto.tmdb.TmdbSearchResultDTO;

import java.util.Optional;

/**
 * Service interface for integrating with The Movie Database (TMDB) API.
 * Provides methods to fetch movie data from external TMDB service.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
public interface TmdbService {
    
    /**
     * Fetches detailed movie information from TMDB by movie ID.
     * 
     * @param tmdbId The TMDB movie ID
     * @return Optional containing TmdbMovieDTO if found, empty otherwise
     */
    Optional<TmdbMovieDTO> getMovieFromTmdb(Long tmdbId);
    
    /**
     * Searches movies in TMDB by query string.
     * 
     * @param query Search query (movie title or keywords)
     * @param page Page number for pagination (default: 1)
     * @return TmdbSearchResultDTO containing paginated search results
     */
    TmdbSearchResultDTO searchMoviesFromTmdb(String query, Integer page);
    
    /**
     * Fetches popular movies from TMDB.
     * 
     * @param page Page number for pagination (default: 1)
     * @return TmdbSearchResultDTO containing popular movies
     */
    TmdbSearchResultDTO getPopularMovies(Integer page);
    
    /**
     * Fetches now playing movies from TMDB.
     * 
     * @param page Page number for pagination (default: 1)
     * @return TmdbSearchResultDTO containing now playing movies
     */
    TmdbSearchResultDTO getNowPlayingMovies(Integer page);
    
    /**
     * Fetches upcoming movies from TMDB.
     * 
     * @param page Page number for pagination (default: 1)
     * @return TmdbSearchResultDTO containing upcoming movies
     */
    TmdbSearchResultDTO getUpcomingMovies(Integer page);
    
    /**
     * Fetches trailer URL for a movie from TMDB.
     * 
     * @param tmdbId The TMDB movie ID
     * @return Optional containing YouTube trailer URL if found
     */
    Optional<String> getMovieTrailerUrl(Long tmdbId);
}
