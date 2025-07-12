package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.FestaRamaRequest;
import com.cinerama.backend.entity.FestaRama;
import com.cinerama.backend.exception.repository.FestaRamaRepository;
import com.cinerama.backend.service.FestaRamaService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Service implementation for managing FestaRama reservations.
 *
 * <p>This service handles the business logic for processing FestaRama reservations,
 * including saving reservation details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>procesarReserva:</b> Processes a FestaRama reservation and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle FestaRama-related operations.</p>
 *
 * @see FestaRamaRepository
 * @see com.cinerama.backend.dto.FestaRamaRequest
 */

@Service
@RequiredArgsConstructor
public class FestaRamaServiceImpl implements FestaRamaService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleaf;
    private final FestaRamaRepository festaRamaRepository;

    @Override
    /* === Processes a reservation and sends a summary email === */
    public void procesarReserva(FestaRamaRequest req) throws MessagingException {
        /* === Save to DB === */
        FestaRama festarama = new FestaRama();
        festarama.setPackageType(req.getPackageType());
        festarama.setCinema(req.getCinema());
        festarama.setMovie(req.getMovie());
        festarama.setDate(req.getDate());
        festarama.setTime(req.getTime());
        festarama.setAttendees(req.getAttendees());
        festarama.setBirthdayChildName(req.getBirthdayChildName());
        festarama.setBirthdayAge(req.getBirthdayAge());
        festarama.setContactName(req.getContactName());
        festarama.setContactEmail(req.getContactEmail());
        festarama.setContactPhone(req.getContactPhone());
        festarama.setMessage(req.getMessage());
        festaRamaRepository.save(festarama);

        /* === Prepare Thymeleaf context with reservation data === */
        Context context = new Context();
        context.setVariable("festarama", req);

        /* === Generate HTML content from template === */
        String html = thymeleaf.process("festarama-summary-email.html", context);

        /* === Create and configure the email message === */
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

        /* === Validate required fields === */
        if (req.getContactEmail() == null || req.getContactEmail().isEmpty()) {
            throw new MessagingException("El correo de contacto es nulo o vacío");
        }

        /* === Set recipient, subject, and content === */
        helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
        helper.setTo(req.getContactEmail());
        helper.setSubject("Resumen de tu FestaRama en Cinerama");
        helper.setText(html, true);

        /* === Send the email === */
        try {
            mailSender.send(mime);
            System.out.println("✅ Correo enviado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            throw new MessagingException("Falló el envío: " + e.getMessage(), e);
        }
    }
}
