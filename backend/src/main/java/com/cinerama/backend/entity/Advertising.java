package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents the Advertising entity in the Cinerama system.
 *
 * <p>This class maps to the `advertising` table in the database and defines
 * the structure of the advertising data, including details about the cinema,
 * advertising type, contact information, and timestamps.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>id:</b> Primary key for the Advertising entity (auto-generated)</li>
 *   <li><b>cinema:</b> Name of the cinema where the advertising will take place</li>
 *   <li><b>advertisingType:</b> Type of advertising selected</li>
 *   <li><b>category:</b> Category of the advertising</li>
 *   <li><b>duration:</b> Duration of the advertising</li>
 *   <li><b>startDate:</b> Start date of the advertising</li>
 *   <li><b>endDate:</b> End date of the advertising</li>
 *   <li><b>budget:</b> Budget allocated for the advertising</li>
 *   <li><b>requirements:</b> Specific requirements for the advertising</li>
 *   <li><b>contactName:</b> Name of the contact person</li>
 *   <li><b>contactEmail:</b> Email address of the contact person</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person</li>
 *   <li><b>company:</b> Company associated with the request</li>
 *   <li><b>message:</b> Additional message or information about the request</li>
 *   <li><b>createdAt:</b> Timestamp indicating when the entity was created</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This entity is used to persist advertising data in the database and is
 * managed by the JPA repository.</p>
 *
 * @see jakarta.persistence.Entity
 * @see org.hibernate.annotations.CreationTimestamp
 */

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
