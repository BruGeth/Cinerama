package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "advertising")
@Getter
@Setter
public class Advertising {

    /* === Primary key for the Advertising entity === */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* === Cinema name === */
    private String cinema;

    /* === Advertising step === */
    private String advertisingType;

    /* === Category of the advertising === */
    private String category;

    /* === Duration  === */
    private String duration;

    /* === Details step === */
    private String startDate;
    private String endDate;
    private String budget;
    private String requirements;

    /* === Contact Information === */
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String company;
    private String message;

    /* === Timestamp indicating when the entity was created === */
    @CreationTimestamp
    private LocalDateTime createdAt;

}
