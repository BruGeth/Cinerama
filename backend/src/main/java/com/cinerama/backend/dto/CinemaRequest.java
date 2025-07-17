package com.cinerama.backend.dto;

import com.cinerama.backend.enums.CinemaStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Cinema request operations.
 * 
 * <p>This class represents the data structure used for creating
 * and updating Cinema entities through API requests.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaRequest {
    
    /**
     * Name of the cinema.
     * Must not be blank.
     */
    @NotBlank(message = "Cinema name is required")
    private String name;
    
    /**
     * Physical address of the cinema.
     * Must not be blank.
     */
    @NotBlank(message = "Cinema address is required")
    private String address;
    
    /**
     * City where the cinema is located.
     * Must not be blank.
     */
    @NotBlank(message = "Cinema city is required")
    private String city;
    
    /**
     * Contact phone number of the cinema.
     * Must not be blank.
     */
    @NotBlank(message = "Cinema phone is required")
    private String phone;
    
    /**
     * Contact email address of the cinema.
     * Must be a valid email format.
     */
    @Email(message = "Email should be valid")
    @NotBlank(message = "Cinema email is required")
    private String email;
    
    /**
     * Current status of the cinema.
     * Must not be null.
     */
    @NotNull(message = "Cinema status is required")
    private CinemaStatus status;
}
