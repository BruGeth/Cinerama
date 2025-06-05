package com.cinerama.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Seat {
    @Id
    private Long id;

    private String seatNumber;
    private boolean available;

    @ManyToOne
    private Show show;
}
