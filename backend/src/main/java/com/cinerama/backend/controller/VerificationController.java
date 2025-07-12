package com.cinerama.backend.controller;

import com.cinerama.backend.entity.User;
import com.cinerama.backend.exception.repository.UserRepository;
import com.cinerama.backend.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user verification operations.
 * Provides endpoints to send verification codes to users.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;

    /**
     * Enndpoint to send a verification code to the user's email.
     * @param email
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        verificationTokenService.createVerificationToken(user);
        return ResponseEntity.ok("Verification code sent to " + email);
    }
}