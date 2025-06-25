package com.cinerama.backend.service;

import com.cinerama.backend.entity.User;

public interface PasswordResetService {
    void createPasswordResetToken(User user);
    void resetPassword(String token, String newPassword);
}