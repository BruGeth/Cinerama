package com.cinerama.backend.controller;
import com.cinerama.backend.dto.FestaRamaRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.cinerama.backend.service.FestaRamaService;

import java.util.Map;

@RestController
@RequestMapping("/api/festarama")
@RequiredArgsConstructor
public class FestaRamaController {
    /* === Service to handle FestaRama business logic === */
    private final FestaRamaService service;

    /* === Endpoint to reserve a FestaRama and send confirmation email === */
    @PostMapping
        public ResponseEntity<Map<String, String>> reservarFesta(@RequestBody FestaRamaRequest request) {
            try {
                service.procesarReserva(request);
                return ResponseEntity.ok(Map.of(
                        "message", "🎉 ¡Solicitud enviada exitosamente!"
                ));
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "message", "❌ Error al enviar correo: " + e.getMessage()
                ));
            }
        }
    }
