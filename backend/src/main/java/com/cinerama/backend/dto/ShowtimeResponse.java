package com.cinerama.backend.dto;

import lombok.Data;

@Data
public class ShowtimeResponse {
    private Long id;
    private String movieTitle; // Solo el nombre de la película
    private String auditorium;
    private java.time.LocalDateTime startTime;
}
