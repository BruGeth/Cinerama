package com.cinerama.backend.dto;

import lombok.Data;

@Data
public class ShowtimeRequest {
    private Long movieId; // Solo el id de la película
    private String auditorium;
    private java.time.LocalDateTime startTime;
}