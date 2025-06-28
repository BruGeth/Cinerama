package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "special_function")
@Getter
@Setter
public class SpecialFunction {

    /* === Unique identifier for the special function === */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* === Name of the cinema where the function takes place === */
    private String cinema;

    /* === Movie selected for the special function === */
    private String movie;

    /* === Date and time of the special function === */
    private String date;

    /* === Time of the special function === */
    private String time;

    /* === Number of attendees expected for the special function === */
    private Integer attendees;

    /* === Additional requirements or notes for the special function === */
    private String requirements;

    /* === Contact information for the person organizing the special function === */
    private String contactName;

    /* === Email of the contact person === */
    private String contactEmail;

    /* === Phone number of the contact person === */
    private String contactPhone;

    /* === Company organizing the special function === */
    private String company;

    /* === Message or description of the special function === */
    private String message;

    /* === Timestamp when the special function was created === */
    @CreationTimestamp
    private LocalDateTime createdAt;
}
