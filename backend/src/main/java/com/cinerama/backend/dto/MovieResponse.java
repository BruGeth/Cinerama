package com.cinerama.backend.dto;

import lombok.Data;

@Data
public class MovieResponse {
    private Long id;
    private String title;
    private String descriptionShowtimes;
    private String descriptionMovie;
    private String rating;
    private String genreName; // Solo el nombre del género
    private String imageUrl;
    private Integer duration;
    private String trailerUrl;
}