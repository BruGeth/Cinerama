package com.cinerama.backend.controller;

import com.cinerama.backend.dto.SpecialFunctionRequest;
import com.cinerama.backend.service.SpecialFunctionService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/specialfunctions")
@RequiredArgsConstructor
public class SpecialFunctionController {

    /* === Injected service to process event logic === */
    private final SpecialFunctionService service;

    /* === Handles POST requests to reserve a special function === */
    @PostMapping
    public ResponseEntity<Map<String, String>> reservarFuncion(@RequestBody SpecialFunctionRequest request) {
        try {
            /* === Calls service to process the special function request === */
            service.processFunction(request);

            /* === Returns success message if processing is successful === */
            return ResponseEntity.ok(Map.of(
                    "message", "🎉 ¡Solicitud enviada exitosamente!"
            ));
        } catch (MessagingException e) {

            /* === Returns error message if email sending fails === */
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "❌ Error al enviar correo: " + e.getMessage()
            ));
        }
    }
}
