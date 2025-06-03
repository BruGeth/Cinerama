package com.cinerama.backend.service;

/**
 * Email service contract for sending system-generated messages.
 */
public interface MailService {
    /**
     * Sends account verification email with activation code.
     *
     * @param toEmail recipient's email address
     * @param verificationCode unique activation code for account verification
     * @throws RuntimeException if email delivery fails
     */
    void sendVerificationEmail(String toEmail, String verificationCode);
}
