package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    // === Event step ===
    private String eventType;

    // === Cinema step ===
    private String cinema;

    // === Details step ===
    private String date;
    private String time;
    private String duration;
    private Integer attendees;
    private String requirements;

    // === Contact step ===
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String company;
    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
