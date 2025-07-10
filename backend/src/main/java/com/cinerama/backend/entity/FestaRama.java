package com.cinerama.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Represents the FestaRama entity in the Cinerama system.
 *
 * <p>This class maps to the `festarama` table in the database and defines
 * the structure of the FestaRama event data, including details about the
 * package type, cinema, movie, attendees, contact information, and timestamps.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>id:</b> Primary key for the FestaRama entity (auto-generated)</li>
 *   <li><b>packageType:</b> Type of package selected for the event</li>
 *   <li><b>cinema:</b> Name of the cinema where the event will take place</li>
 *   <li><b>movie:</b> Movie chosen for the event</li>
 *   <li><b>date:</b> Date of the event</li>
 *   <li><b>time:</b> Time of the event</li>
 *   <li><b>attendees:</b> Number of attendees</li>
 *   <li><b>birthdayChildName:</b> Name of the birthday child</li>
 *   <li><b>birthdayAge:</b> Age of the birthday child</li>
 *   <li><b>contactName:</b> Name of the contact person</li>
 *   <li><b>contactEmail:</b> Email address of the contact person</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person</li>
 *   <li><b>message:</b> Additional message or comments about the event</li>
 *   <li><b>createdAt:</b> Timestamp indicating when the entity was created</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This entity is used to persist FestaRama event data in the database and is
 * managed by the JPA repository.</p>
 *
 * @see jakarta.persistence.Entity
 * @see org.hibernate.annotations.CreationTimestamp
 */

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
