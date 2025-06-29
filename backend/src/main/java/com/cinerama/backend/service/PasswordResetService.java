package com.cinerama.backend.service;

import com.cinerama.backend.entity.User;

public interface PasswordResetService {
    void createPasswordResetToken(User user);
    void validatePasswordResetToken(String email, String token);
    void changePassword(String email, String newPassword, String confirmPassword);
}