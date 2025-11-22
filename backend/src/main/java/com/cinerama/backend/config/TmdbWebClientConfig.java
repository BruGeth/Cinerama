package com.cinerama.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for TMDB API WebClient.
 * Creates a configured WebClient bean for making requests to The Movie Database API.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Configuration
@Slf4j
public class TmdbWebClientConfig {
    
    @Value("${tmdb.base-url:https://api.themoviedb.org/3}")
    private String tmdbBaseUrl;
    
    @Value("${tmdb.api-key}")
    private String tmdbApiKey;
    
    /**
     * Creates a configured WebClient bean for TMDB API.
     * 
     * @return WebClient configured with base URL and default headers
     */
    @Bean(name = "tmdbWebClient")
    public WebClient tmdbWebClient() {
        log.info("🎬 Initializing TMDB WebClient with base URL: {}", tmdbBaseUrl);
        
        return WebClient.builder()
                .baseUrl(tmdbBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
