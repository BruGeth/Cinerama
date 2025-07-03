package com.cinerama.backend.dto;

import lombok.Data;

/**
 * Represents a request to book a FestaRama party in the Cinerama system.
 *
 * <p>This class defines the structure of the data required to process a FestaRama
 * party booking request. It includes details about the party, the cinema, and the
 * contact information.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>packageType:</b> Type of package selected for the party (required)</li>
 *   <li><b>cinema:</b> Name of the cinema where the party will take place (required)</li>
 *   <li><b>movie:</b> Movie chosen for the party (optional)</li>
 *   <li><b>date:</b> Date of the party in YYYY-MM-DD format (required)</li>
 *   <li><b>time:</b> Time of the party in HH:mm format (required)</li>
 *   <li><b>attendees:</b> Number of attendees (minimum value: 1)</li>
 *   <li><b>birthdayChildName:</b> Name of the birthday child (optional)</li>
 *   <li><b>birthdayAge:</b> Age of the birthday child (optional)</li>
 *   <li><b>contactName:</b> Name of the contact person (required)</li>
 *   <li><b>contactEmail:</b> Email address of the contact person (required, must be valid)</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person (required)</li>
 *   <li><b>message:</b> Additional message or comments (optional)</li>
 * </ul>
 *
 * <h2>Validation:</h2>
 * <p>Fields marked as required must be provided and meet the specified constraints.
 * For example, the email must be in a valid format, and the number of attendees must be at least 1.</p>
 *
 * <h2>Usage:</h2>
 * <p>This DTO is used in the `FestaRamaController` to handle incoming FestaRama booking requests.</p>
 *
 * @see com.cinerama.backend.controller.FestaRamaController
 * @see com.cinerama.backend.service.FestaRamaService
 */

@Data
public class FestaRamaRequest {

    /* Type of package selected for the party */
    private String packageType;

    /* Cinema where the party will take place */
    private String cinema;

    /* Movie chosen for the party */
    private String movie;

    /* Date of the party */
    private String date;

    /* Time of the party */
    private String time;

    /* Number of attendees */
    private Integer attendees;

    /* Details about the birthday child */
    private String birthdayChildName;

    /* Age of the birthday child */
    private Integer birthdayAge;

    /* Person's contacts */
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    /* Additional message or comments */
    private String message;
}
