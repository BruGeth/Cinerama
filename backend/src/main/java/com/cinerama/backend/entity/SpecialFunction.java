package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents the SpecialFunction entity in the Cinerama system.
 *
 * <p>This class maps to the `special_function` table in the database and defines
 * the structure of the special function data, including details about the cinema,
 * movie, attendees, contact information, and timestamps.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>id:</b> Primary key for the SpecialFunction entity (auto-generated)</li>
 *   <li><b>cinema:</b> Name of the cinema where the function takes place</li>
 *   <li><b>movie:</b> Movie selected for the special function</li>
 *   <li><b>date:</b> Date of the special function</li>
 *   <li><b>time:</b> Time of the special function</li>
 *   <li><b>attendees:</b> Number of attendees expected for the special function</li>
 *   <li><b>requirements:</b> Additional requirements or notes for the special function</li>
 *   <li><b>contactName:</b> Name of the contact person</li>
 *   <li><b>contactEmail:</b> Email address of the contact person</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person</li>
 *   <li><b>company:</b> Company organizing the special function</li>
 *   <li><b>message:</b> Message or description of the special function</li>
 *   <li><b>createdAt:</b> Timestamp indicating when the entity was created</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This entity is used to persist special function data in the database and is
 * managed by the JPA repository.</p>
 *
 * @see jakarta.persistence.Entity
 * @see org.hibernate.annotations.CreationTimestamp
 */

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
