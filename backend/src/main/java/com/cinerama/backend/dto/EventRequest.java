package com.cinerama.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Represents a request to create an event in the Cinerama system.
 *
 * <p>This class defines the structure of the data required to process an event request.
 * It includes details about the event, the cinema, and the contact information.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>eventType:</b> Type of the event (e.g., conference, special screening) (required)</li>
 *   <li><b>cinema:</b> Name of the cinema where the event will take place (required)</li>
 *   <li><b>date:</b> Date of the event in YYYY-MM-DD format (required)</li>
 *   <li><b>time:</b> Time of the event in HH:mm format (required)</li>
 *   <li><b>duration:</b> Duration of the event (optional)</li>
 *   <li><b>attendees:</b> Expected number of attendees (minimum value: 1)</li>
 *   <li><b>requirements:</b> Specific requirements for the event (optional)</li>
 *   <li><b>contactName:</b> Name of the contact person (required)</li>
 *   <li><b>contactEmail:</b> Email address of the contact person (required, must be valid)</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person (required)</li>
 *   <li><b>company:</b> Name of the organizing company (optional)</li>
 *   <li><b>message:</b> Additional message or notes related to the event (optional)</li>
 * </ul>
 *
 * <h2>Validation:</h2>
 * <p>Fields marked as required must be provided and meet the specified constraints.
 * For example, the email must be in a valid format, and the number of attendees must be at least 1.</p>
 *
 * <h2>Usage:</h2>
 * <p>This DTO is used in the `EventController` to handle incoming event creation requests.</p>
 *
 * @see com.cinerama.backend.controller.EventController
 * @see com.cinerama.backend.service.EventService
 */

@Data
public class EventRequest {
    /* === Event step === */
    @NotBlank
    private String eventType;
    /* === Cinema step === */
    @NotBlank
    private String cinema;
    /* === Details step === */
    @NotBlank
    private String date;
    @NotBlank
    private String time;
    private String duration;    // optional
    @Min(1)
    private Integer attendees;
    private String requirements;
    /* === Contact step === */
    @NotBlank
    private String contactName;
    @Email
    private String contactEmail;
    @NotBlank
    private String contactPhone;
    private String company;
    private String message;
}
