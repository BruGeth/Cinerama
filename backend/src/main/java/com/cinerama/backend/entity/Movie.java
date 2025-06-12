package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String descriptionShowtimes; // Short description for showtimes

    @Column(length = 1000)
    private String descriptionMovie; // Larger description for movie details
    @Column(nullable = false)
    private String rating; // Example: "PG-13", "R", etc.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column
    private String imageUrl; // Route or URL to the movie poster

    @Column
    private Integer duration; // Duration in minutes

    @Column
    private String trailerUrl; // URL to the movie trailer

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;
}