package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.tmdb.TmdbMovieDTO;
import com.cinerama.backend.dto.tmdb.TmdbSearchResultDTO;
import com.cinerama.backend.dto.tmdb.TmdbVideoDTO;
import com.cinerama.backend.dto.tmdb.TmdbVideoResultDTO;
import com.cinerama.backend.service.TmdbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Implementation of TmdbService for integrating with TMDB API.
 * Handles all external API calls to The Movie Database.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Service
@Slf4j
public class TmdbServiceImpl implements TmdbService {
    
    private final WebClient tmdbWebClient;
    
    @Value("${tmdb.api-key}")
    private String apiKey;
    
    @Value("${tmdb.language:es-MX}")
    private String language;
    
    public TmdbServiceImpl(@Qualifier("tmdbWebClient") WebClient tmdbWebClient) {
        this.tmdbWebClient = tmdbWebClient;
    }
    
    @Override
    public Optional<TmdbMovieDTO> getMovieFromTmdb(Long tmdbId) {
        log.info("🎬 Fetching movie details from TMDB for ID: {}", tmdbId);
        
        try {
            TmdbMovieDTO movie = tmdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/{id}")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build(tmdbId))
                    .retrieve()
                    .bodyToMono(TmdbMovieDTO.class)
                    .block();
            
            log.info("✅ Successfully fetched movie from TMDB: {}", movie != null ? movie.getTitle() : "null");
            return Optional.ofNullable(movie);
            
        } catch (WebClientResponseException.NotFound e) {
            log.warn("⚠️ Movie not found in TMDB with ID: {}", tmdbId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("❌ Error fetching movie from TMDB: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    @Override
    public TmdbSearchResultDTO searchMoviesFromTmdb(String query, Integer page) {
        log.info("🔍 Searching movies in TMDB with query: '{}', page: {}", query, page);
        
        final Integer finalPage = (page == null || page < 1) ? 1 : page;
        
        try {
            TmdbSearchResultDTO result = tmdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .queryParam("query", query)
                            .queryParam("page", finalPage)
                            .queryParam("include_adult", false)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResultDTO.class)
                    .block();
            
            log.info("✅ Search completed. Found {} results", 
                    result != null ? result.getTotalResults() : 0);
            return result != null ? result : TmdbSearchResultDTO.builder()
                    .page(finalPage)
                    .results(java.util.Collections.emptyList())
                    .totalResults(0)
                    .totalPages(0)
                    .build();
            
        } catch (Exception e) {
            log.error("❌ Error searching movies in TMDB: {}", e.getMessage(), e);
            return TmdbSearchResultDTO.builder()
                    .page(finalPage)
                    .results(java.util.Collections.emptyList())
                    .totalResults(0)
                    .totalPages(0)
                    .build();
        }
    }
    
    @Override
    public TmdbSearchResultDTO getPopularMovies(Integer page) {
        log.info("🌟 Fetching popular movies from TMDB, page: {}", page);
        
        final Integer finalPage = (page == null || page < 1) ? 1 : page;
        
        try {
            TmdbSearchResultDTO result = tmdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/popular")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .queryParam("page", finalPage)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResultDTO.class)
                    .block();
            
            log.info("✅ Fetched {} popular movies", 
                    result != null && result.getResults() != null ? result.getResults().size() : 0);
            return result != null ? result : createEmptyResult(finalPage);
            
        } catch (Exception e) {
            log.error("❌ Error fetching popular movies from TMDB: {}", e.getMessage(), e);
            return createEmptyResult(finalPage);
        }
    }
    
    @Override
    public TmdbSearchResultDTO getNowPlayingMovies(Integer page) {
        log.info("🎥 Fetching now playing movies from TMDB, page: {}", page);
        
        final Integer finalPage = (page == null || page < 1) ? 1 : page;
        
        try {
            TmdbSearchResultDTO result = tmdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/now_playing")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .queryParam("page", finalPage)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResultDTO.class)
                    .block();
            
            log.info("✅ Fetched {} now playing movies", 
                    result != null && result.getResults() != null ? result.getResults().size() : 0);
            return result != null ? result : createEmptyResult(finalPage);
            
        } catch (Exception e) {
            log.error("❌ Error fetching now playing movies from TMDB: {}", e.getMessage(), e);
            return createEmptyResult(finalPage);
        }
    }
    
    @Override
    public TmdbSearchResultDTO getUpcomingMovies(Integer page) {
        log.info("📅 Fetching upcoming movies from TMDB, page: {}", page);
        
        final Integer finalPage = (page == null || page < 1) ? 1 : page;
        
        try {
            TmdbSearchResultDTO result = tmdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/upcoming")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .queryParam("page", finalPage)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResultDTO.class)
                    .block();
            
            log.info("✅ Fetched {} upcoming movies", 
                    result != null && result.getResults() != null ? result.getResults().size() : 0);
            return result != null ? result : createEmptyResult(finalPage);
            
        } catch (Exception e) {
            log.error("❌ Error fetching upcoming movies from TMDB: {}", e.getMessage(), e);
            return createEmptyResult(finalPage);
        }
    }
    
    @Override
    public Optional<String> getMovieTrailerUrl(Long tmdbId) {
        log.info("🎬 Fetching trailer for TMDB movie ID: {}", tmdbId);
        
        try {
            TmdbVideoResultDTO videoResult = tmdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/{id}/videos")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build(tmdbId))
                    .retrieve()
                    .bodyToMono(TmdbVideoResultDTO.class)
                    .block();
            
            if (videoResult != null && videoResult.getResults() != null) {
                // Find first official trailer
                Optional<String> trailerUrl = videoResult.getResults().stream()
                        .filter(video -> "Trailer".equalsIgnoreCase(video.getType()) 
                                && Boolean.TRUE.equals(video.getOfficial()))
                        .findFirst()
                        .map(TmdbVideoDTO::getYoutubeUrl);
                
                if (trailerUrl.isPresent()) {
                    log.info("✅ Found official trailer: {}", trailerUrl.get());
                    return trailerUrl;
                }
                
                // If no official trailer, try any YouTube trailer
                trailerUrl = videoResult.getResults().stream()
                        .filter(video -> "Trailer".equalsIgnoreCase(video.getType()))
                        .findFirst()
                        .map(TmdbVideoDTO::getYoutubeUrl);
                
                if (trailerUrl.isPresent()) {
                    log.info("✅ Found trailer: {}", trailerUrl.get());
                    return trailerUrl;
                }
            }
            
            log.warn("⚠️ No trailer found for movie ID: {}", tmdbId);
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("❌ Error fetching trailer from TMDB: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    /**
     * Helper method to create an empty result.
     */
    private TmdbSearchResultDTO createEmptyResult(Integer page) {
        return TmdbSearchResultDTO.builder()
                .page(page)
                .results(java.util.Collections.emptyList())
                .totalResults(0)
                .totalPages(0)
                .build();
    }
}
