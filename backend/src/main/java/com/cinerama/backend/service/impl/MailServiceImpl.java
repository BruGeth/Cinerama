package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.mail.MailContentBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder contentBuilder;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, MailContentBuilder contentBuilder) {
        this.mailSender = mailSender;
        this.contentBuilder = contentBuilder;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verificación de Cuenta - Cinerama";
        String content = contentBuilder.buildVerificationEmail(toEmail, verificationCode); // renders Thymeleaf template

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
