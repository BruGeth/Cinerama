package com.cinerama.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "festarama")
@Getter
@Setter
public class FestaRama {

    /* === Primary key with auto-increment === */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* === Type of package selected for the event === */
    private String packageType;

    /* === Cinema where the event will take place === */
    private String cinema;

    /* === Movie chosen for the event === */
    private String movie;

    /* === Date of the event === */
    private String date;

    /* === Time of the event === */
    private String time;

    /* === Number of attendees === */
    private Integer attendees;

    /* === Details about the birthday child === */
    private String birthdayChildName;

    /* === Age of the birthday child === */
    private Integer birthdayAge;

    /* === Person's contacts === */
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    /* === Additional message or comments === */
    private String message;

    /* === Timestamp when the entity was created === */
    @CreationTimestamp
    private LocalDateTime createdAt;
}
