package com.cinerama.backend.entity;

import com.cinerama.backend.enums.MovieStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String descriptionShowtimes; // Descripción corta para cartelera

    @Column(length = 1000)
    private String descriptionMovie; // Descripción larga para detalles

    @Column(nullable = false)
    private Integer duration; // Duración en minutos

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column
    private String director;

    @ElementCollection
    private List<String> cast;

    @Column(nullable = false)
    private String rating; // Ejemplo: "PG-13", "R", etc.

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column
    private String trailerUrl; // URL del tráiler

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MovieStatus status; // COMING_SOON, NOW_PLAYING, ENDED

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}