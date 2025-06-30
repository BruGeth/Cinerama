package com.cinerama.backend.controller;

import com.cinerama.backend.dto.AdvertisingRequest;
import com.cinerama.backend.service.AdvertisingService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/advertising")
@RequiredArgsConstructor

public class AdvertisingController {

    /* === Injected service to process event logic === */
    private final AdvertisingService service;

    /* === Endpoint to submit advertising requests === */
    @PostMapping
    public ResponseEntity<Map<String, String>> submitAdvertising(@RequestBody AdvertisingRequest request) {
        try {

            /* === Process the advertising request === */
            service.processAdvertising(request);

            /* === Return success response === */
            return ResponseEntity.ok(Map.of("message", "🎉 ¡Solicitud enviada exitosamente!"));
        } catch (MessagingException e) {

            /* === Handle email sending errors === */
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "❌ Error al enviar correo: " + e.getMessage()));
        }
    }
}
