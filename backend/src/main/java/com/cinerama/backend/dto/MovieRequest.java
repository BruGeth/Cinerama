package com.cinerama.backend.dto;

import lombok.Data;

@Data
public class MovieRequest {
    private String title;
    private String descriptionShowtimes;
    private String descriptionMovie;
    private String rating;
    private Long genreId; // Solo el id del género
    private String imageUrl;
    private Integer duration;
    private String trailerUrl;
}