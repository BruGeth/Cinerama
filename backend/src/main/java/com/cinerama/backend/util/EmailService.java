package com.cinerama.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Cinerama - Account Verification";
        String body = "Hello,\n\n" +
                "Please verify your account using the following code:\n\n" +
                verificationCode + "\n\n" +
                "Thank you for registering with Cinerama!";

        // Simulate sending email (printing to console/log)
        log.info("Simulating email sending...");
        log.info("To: {}", toEmail);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
    }
}
