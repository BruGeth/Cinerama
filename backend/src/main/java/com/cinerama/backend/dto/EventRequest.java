package com.cinerama.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

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
