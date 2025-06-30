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

/**
 * Controller for handling password reset operations.
 * Provides endpoints to initiate password reset, validate reset tokens, and change passwords.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    /**
     * Endpoint to initiate a password reset by sending a recovery email.
     * @param request Contains the user's email for password reset.
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        passwordResetService.createPasswordResetToken(user);
        return ResponseEntity.ok("Recovery email sent successfully");
    }
    /**
     * Endpoint to validate a password reset token.
     * @param request Contains the user's email and the reset token.
     * @return ResponseEntity with a success message if the token is valid.
     */
    @PostMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@Valid @RequestBody ValidateResetTokenRequest request) {
        passwordResetService.validatePasswordResetToken(request.getEmail(), request.getToken());
        return ResponseEntity.ok("Valid token");
    }
    /**
     * Endpoint to change the user's password.
     * @param request Contains the user's email, new password, and confirmation password.
     * @return ResponseEntity with a success message if the password is changed successfully.
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        passwordResetService.changePassword(request.getEmail(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}