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

    /* === Primary key for Event === */
    @Id
    @GeneratedValue
    private Long id;

    /* === Type of the event === */
    private String eventType;

    /* === Cinema where the event takes place === */
    private String cinema;

    /* === Date, Time and Duration of the event === */
    private String date;
    private String time;
    private String duration;

    /* === Number of expected attendees === */
    private Integer attendees;

    /* === Special requirements for the event === */
    private String requirements;

    /* === Contact Step === */
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String company;
    private String message;

    /* === Timestamp when the event was created === */
    @CreationTimestamp
    private LocalDateTime createdAt;
}
