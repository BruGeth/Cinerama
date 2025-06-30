package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.AdvertisingRequest;
import com.cinerama.backend.entity.Advertising;
import com.cinerama.backend.repository.AdvertisingRepository;
import com.cinerama.backend.service.AdvertisingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class AdvertisingServiceImpl implements AdvertisingService {

    /* === Injected repository to handle advertising persistence === */
    private final AdvertisingRepository repository;

    /* === Injected mail sender to send emails === */
    private final JavaMailSender mailSender;

    /* === Injected Thymeleaf template engine for email templates === */
    private final SpringTemplateEngine thymeleaf;

    @Override
    public void processAdvertising(AdvertisingRequest req) throws MessagingException {

        /* === Create and populate Advertising entity from request === */
        Advertising adv = new Advertising();
        adv.setCinema(req.getCinema());
        adv.setAdvertisingType(req.getAdvertisingType());
        adv.setCategory(req.getCategory());
        adv.setDuration(req.getDuration());
        adv.setStartDate(req.getStartDate());
        adv.setEndDate(req.getEndDate());
        adv.setBudget(req.getBudget());
        adv.setRequirements(req.getRequirements());
        adv.setContactName(req.getContactName());
        adv.setContactEmail(req.getContactEmail());
        adv.setContactPhone(req.getContactPhone());
        adv.setCompany(req.getCompany());
        adv.setMessage(req.getMessage());

        /* === Save advertising entity to the database === */
        repository.save(adv);

        /* === Validate recipient's email=== */
        String to = req.getContactEmail();
        if (to == null || to.trim().isEmpty()) {
            throw new MessagingException("El correo electrónico del contacto está vacío.");
        }

        /* === Prepare email context with advertising request data === */
        Context context = new Context();
        context.setVariable("advertising", req);

        /* === Generate email content using Thymeleaf template === */
        String html = thymeleaf.process("advertising-summary-email.html", context);

        /* === Create and configure MIME message for email === */
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
        helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
        helper.setTo(to);
        helper.setSubject("Resumen de tu solicitud de Publicidad - Cinerama");
        helper.setText(html, true);

        /* === Send the email and handle errors === */
        try {
            mailSender.send(mime);
            System.out.println("✅ Correo enviado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            throw new MessagingException("Falló el envío: " + e.getMessage(), e);
        }
    }
}
