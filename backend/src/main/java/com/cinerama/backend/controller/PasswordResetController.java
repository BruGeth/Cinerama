package com.cinerama.backend.controller;

import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        passwordResetService.createPasswordResetToken(user);
        return ResponseEntity.ok("Recovery email sent successfully");
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@RequestParam String email, @RequestParam String token) {
        passwordResetService.validatePasswordResetToken(email, token);
        return ResponseEntity.ok("Valid token");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        passwordResetService.changePassword(email, newPassword, confirmPassword);
        return ResponseEntity.ok("Password changed successfully");
    }
}