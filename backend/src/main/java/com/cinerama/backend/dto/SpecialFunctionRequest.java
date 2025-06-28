package com.cinerama.backend.dto;

import lombok.Data;

@Data
public class SpecialFunctionRequest {

    /* === Name of the cinema where the function will take place === */
    private String cinema;
    /* === Movie selected for the special function === */
    private String movie;
    /* === Details of the special function === */
    private String date;
    /* === Time of the special function === */
    private String time;
    /* === Number of attendees for the special function === */
    private Integer capacity;
    /* === Specific requirements for the special function === */
    private String requirements;
    /* === Name of the contact person for the request === */
    private String contactName;
    /* === Email of the contact person === */
    private String contactEmail;
    /* === Phone number of the contact person === */
    private String contactPhone;
    /* === Company associated with the special function request === */
    private String company;
    /* === Message or additional information regarding the special function request === */
    private String message;
}
