package com.cinerama.backend.dto;

import lombok.Data;

@Data
public class AdvertisingRequest {

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

}
