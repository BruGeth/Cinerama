package com.cinerama.backend.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for TMDB Video (Trailers, Teasers, etc.).
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbVideoDTO {
    
    private String id;
    
    @JsonProperty("iso_639_1")
    private String language;
    
    @JsonProperty("iso_3166_1")
    private String country;
    
    private String key;
    
    private String name;
    
    private String site;
    
    private Integer size;
    
    private String type;
    
    private Boolean official;
    
    @JsonProperty("published_at")
    private String publishedAt;
    
    /**
     * Get YouTube trailer URL if this is a YouTube video.
     */
    public String getYoutubeUrl() {
        if ("YouTube".equalsIgnoreCase(site) && key != null) {
            return "https://www.youtube.com/watch?v=" + key;
        }
        return null;
    }
}
