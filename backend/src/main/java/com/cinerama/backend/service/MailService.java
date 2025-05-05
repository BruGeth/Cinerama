package com.cinerama.backend.service;

public interface MailService {
    void sendVerificationEmail(String toEmail, String verificationCode);
}
