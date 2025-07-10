package com.cinerama.backend.dto;

import lombok.Data;

/**
 * DTO for handling advertising requests.
 *
 * <p>This class represents the structure of an advertising request in the Cinerama system.
 * It includes all the necessary details required to process an advertising proposal.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>cinema:</b> Name of the cinema where the advertising will be displayed</li>
 *   <li><b>advertisingType:</b> Type of advertising (e.g., banner, video, etc.)</li>
 *   <li><b>category:</b> Category of the advertising (e.g., promotion, announcement)</li>
 *   <li><b>duration:</b> Duration of the advertising campaign</li>
 *   <li><b>startDate:</b> Start date of the advertising campaign</li>
 *   <li><b>endDate:</b> End date of the advertising campaign</li>
 *   <li><b>budget:</b> Budget allocated for the advertising</li>
 *   <li><b>requirements:</b> Specific requirements or instructions for the advertising</li>
 *   <li><b>contactName:</b> Name of the contact person</li>
 *   <li><b>contactEmail:</b> Email address of the contact person</li>
 *   <li><b>contactPhone:</b> Phone number of the contact person</li>
 *   <li><b>company:</b> Name of the company submitting the advertising request</li>
 *   <li><b>message:</b> Additional message or notes related to the advertising request</li>
 * </ul>
 *
 * <h2>Validation:</h2>
 * <p>Ensure that all required fields are provided and meet the expected format before processing.</p>
 *
 * <h2>Usage:</h2>
 * <p>This DTO is used in the AdvertisingController to handle incoming advertising requests.</p>
 *
 * @see com.cinerama.backend.controller.AdvertisingController
 * @see com.cinerama.backend.service.AdvertisingService
 */

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
