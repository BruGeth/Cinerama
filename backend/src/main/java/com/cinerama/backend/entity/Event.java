package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents the Event entity in the Cinerama system.
 *
 * <p>This class maps to the `events` table in the database and defines
 * the structure of the event data, including details about the event type,
 * cinema, schedule, contact information, and timestamps.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>id:</b> Primary key for the Event entity (auto-generated)</li>
 *   <li><b>eventType:</b> Type of the event</li>
 *   <li><b>cinema:</b> Name of the cinema where the event takes place</li>
 *   <li><b>date:</b> Date of the event</li>
 *   <li><b>time:</b> Time of the event</li>
 *   <li><b>duration:</b> Duration of the event</li>
 *   <li><b>attendees:</b> Number of expected attendees</li>
 *   <li><b>requirements:</b> Special requirements for the event</li>
 *   <li><b>contactName:</b> Name of the contact person</li>
 *   <li><b>contactEmail:</b> Email address of the contact person</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person</li>
 *   <li><b>company:</b> Company associated with the event</li>
 *   <li><b>message:</b> Additional message or information about the event</li>
 *   <li><b>createdAt:</b> Timestamp indicating when the entity was created</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This entity is used to persist event data in the database and is
 * managed by the JPA repository.</p>
 *
 * @see jakarta.persistence.Entity
 * @see org.hibernate.annotations.CreationTimestamp
 */

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
