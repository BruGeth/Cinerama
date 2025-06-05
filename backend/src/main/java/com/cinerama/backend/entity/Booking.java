package com.cinerama.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime bookingTime;
    private String confirmationCode;

    @ManyToOne
    private Show show;

    @ManyToMany
    private List<Seat> seats;
}
