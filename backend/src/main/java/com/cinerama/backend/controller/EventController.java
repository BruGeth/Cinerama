package com.cinerama.backend.controller;

import com.cinerama.backend.dto.*;
import com.cinerama.backend.service.EventService;
import com.cinerama.backend.dto.EventRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    /* === Injected service to process event logic === */
    private final EventService service;  // ← Interface injection

    /* === Endpoint to create a new event and send notification email === */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request) {

        try {
            /* === Process the event and send email === */
            service.processEvent(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new EventResponse("OK", "🎉 ¡Solicitud enviada exitosamente!"));
        } catch (MessagingException ex) {
            /* === Handle email sending errors === */
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResponse("ERROR", ex.getMessage()));
        }
    }
}
