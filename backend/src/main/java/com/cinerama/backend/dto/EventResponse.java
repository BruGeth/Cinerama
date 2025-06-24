package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventResponse {
    private String status;   // "OK" | "ERROR"
    private String message;  // Optional message for additional context
}
