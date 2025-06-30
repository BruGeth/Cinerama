package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ChangePasswordRequest;
import com.cinerama.backend.dto.ForgotPasswordRequest;
import com.cinerama.backend.dto.ValidateResetTokenRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.PasswordResetService;
import com.cinerama.backend.exception.user.UserNotFoundException;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        passwordResetService.createPasswordResetToken(user);
        return ResponseEntity.ok("Recovery email sent successfully");
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@Valid @RequestBody ValidateResetTokenRequest request) {
        passwordResetService.validatePasswordResetToken(request.getEmail(), request.getToken());
        return ResponseEntity.ok("Valid token");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        passwordResetService.changePassword(request.getEmail(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}