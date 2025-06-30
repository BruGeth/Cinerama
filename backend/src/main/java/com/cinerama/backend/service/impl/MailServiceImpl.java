package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.mail.MailContentBuilder;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.exception.user.UserNotFoundException;
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
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, MailContentBuilder contentBuilder, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.contentBuilder = contentBuilder;
        this.userRepository = userRepository;
    }

    /**
     * Sends a verification email to the user.
     *
     * @param toEmail Recipient's email address.
     * @param verificationCode Unique verification code.
     * @throws UserNotFoundException If the user with the specified email does not exist.
     */
    @Override
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verificación de Cuenta - Cinerama";
        User user = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + toEmail));
        String content = contentBuilder.buildVerificationEmail(user.getName(), verificationCode); // renders Thymeleaf template

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
    /**
     * Sends a password reset email to the user.
     *
     * @param toEmail Recipient's email address.
     * @param resetCode Unique password reset code.
     * @throws UserNotFoundException If the user with the specified email does not exist.
     */
    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        String subject = "Restablecimiento de Contraseña - Cinerama";
        User user = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + toEmail));
        String content = contentBuilder.buildPasswordResetEmail(user.getName(), resetCode); // renders Thymeleaf template

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
