package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.PasswordResetToken;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.PasswordResetTokenRepository;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.PasswordResetService;
import com.cinerama.backend.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeGenerator codeGenerator;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void createPasswordResetToken(User user) {
        String token = codeGenerator.generateCode();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(resetToken);
        mailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void validatePasswordResetToken(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (!resetToken.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Token does not belong to this user");
        }
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expired token");
        }
    }

    @Override
    public void changePassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // Delete all tokens associated with the user
        tokenRepository.deleteAll(tokenRepository.findAllByUser(user));
    }
}