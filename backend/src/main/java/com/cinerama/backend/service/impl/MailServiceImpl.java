package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.mail.MailContentBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Implementation of the email service.
 * Provides methods to send verification emails.
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder contentBuilder;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, MailContentBuilder contentBuilder) {
        this.mailSender = mailSender;
        this.contentBuilder = contentBuilder;
    }

    /**
     * Sends a verification email to the user.
     *
     * @param toEmail Recipient's email address.
     * @param verificationCode Unique verification code.
     * @throws RuntimeException If an error occurs while sending the email.
     */
    @Override
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verificación de Cuenta - Cinerama";
        String content = contentBuilder.buildVerificationEmail(toEmail, verificationCode); // renders Thymeleaf template

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
