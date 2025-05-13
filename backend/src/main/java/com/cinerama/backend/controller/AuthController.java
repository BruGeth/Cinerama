package com.cinerama.backend.controller;

import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.LoginResponse;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerificationRequest request) {
        authService.verify(request);
        return ResponseEntity.ok("Account verified successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);
    }
}
