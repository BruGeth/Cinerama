package com.cinerama.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Show {
    @Id
    private Long id;

    private String movieTitle;
    private LocalDateTime startTime;
}
