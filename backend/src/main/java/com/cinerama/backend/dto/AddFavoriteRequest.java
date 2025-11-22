package com.cinerama.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding a movie to user's favorites.
 * 
 * @author Cinerama Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFavoriteRequest {
    
    /**
     * The ID of the movie to add to favorites.
     */
    @NotNull(message = "Movie ID is required")
    private Long movieId;
}
