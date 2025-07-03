package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.PasswordResetToken;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.exception.auth.ExpiredTokenException;
import com.cinerama.backend.exception.auth.InvalidTokenException;
import com.cinerama.backend.exception.auth.TokenNotBelongUserException;
import com.cinerama.backend.exception.user.PasswordsNotMatchException;
import com.cinerama.backend.exception.user.UserNotFoundException;
import com.cinerama.backend.repository.PasswordResetTokenRepository;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.PasswordResetService;
import com.cinerama.backend.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of the PasswordResetService interface.
 * Handles password reset token creation, validation, and password change operations.
 */
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

    /**
     * Creates a password reset token for the user and sends it via email.
     *
     * @param user The user for whom the password reset token is created.
     */
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

    /**
     * Validates the password reset token for the user.
     *
     * @param email The email of the user.
     * @param token The password reset token to validate.
     * @throws UserNotFoundException if the user with the given email does not exist.
     * @throws InvalidTokenException if the token is invalid.
     * @throws TokenNotBelongUserException if the token does not belong to the user.
     * @throws ExpiredTokenException if the token has expired.
     */
    @Override
    public void validatePasswordResetToken(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        if (!resetToken.getUser().getId().equals(user.getId())) {
            throw new TokenNotBelongUserException("Token does not belong to this user");
        }
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ExpiredTokenException("Expired token");
        }
    }

    /**
     * Changes the user's password using the provided email and new password.
     *
     * @param email The email of the user.
     * @param newPassword The new password to set.
     * @param confirmPassword The confirmation of the new password.
     * @throws PasswordsNotMatchException if the new password and confirmation do not match.
     * @throws UserNotFoundException if the user with the given email does not exist.
     */
    @Override
    public void changePassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordsNotMatchException("Passwords do not match");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // Delete all tokens associated with the user
        tokenRepository.deleteAll(tokenRepository.findAllByUser(user));
    }
}