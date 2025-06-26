package com.cinerama.backend.dto;

import lombok.Data;

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
