package com.cinerama.backend.service.impl;

import com.cinerama.backend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Cinerama - Account Verification";
        String body = "Hello,\n\n" +
                "Please verify your account using the following code:\n\n" +
                verificationCode + "\n\n" +
                "Thank you for registering with Cinerama!";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
