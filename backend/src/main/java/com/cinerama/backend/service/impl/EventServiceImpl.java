package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.EventRequest;
import com.cinerama.backend.entity.Event;
import com.cinerama.backend.repository.EventRepository;
import com.cinerama.backend.service.EventService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Service implementation for managing Event requests.
 *
 * <p>This service handles the business logic for processing Event requests,
 * including saving event details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>processEvent:</b> Processes an Event request and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle Event-related operations.</p>
 *
 * @see com.cinerama.backend.repository.EventRepository
 * @see com.cinerama.backend.dto.EventRequest
 */

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    /* === Dependencies for email sending, template processing, and database operations === */
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleaf;
    private final EventRepository eventRepository;

    /* === Method to process event requests and send summary emails === */
    @Override
    public void processEvent(EventRequest req) throws MessagingException {
        // Save event details to the database
        Event event = new Event();
        event.setEventType(req.getEventType());
        event.setCinema(req.getCinema());
        event.setDate(req.getDate());
        event.setTime(req.getTime());
        event.setDuration(req.getDuration());
        event.setAttendees(req.getAttendees());
        event.setRequirements(req.getRequirements());
        event.setContactName(req.getContactName());
        event.setContactEmail(req.getContactEmail());
        event.setContactPhone(req.getContactPhone());
        event.setCompany(req.getCompany());
        event.setMessage(req.getMessage());
        eventRepository.save(event);

        // Create HTML content using Thymeleaf template engine
        Context ctx = new Context();
        ctx.setVariable("event", req);
        String html = thymeleaf.process("event-summary-email.html", ctx);

        //Prepare and send email
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

        helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername()); // IMPORTANT: Sender email
        helper.setTo(req.getContactEmail());
        helper.setSubject("Resumen de tu evento en Cinerama");
        helper.setText(html, true); // true = HTML content

        try {
            mailSender.send(mime);
            System.out.println("✅ Correo enviado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            throw new MessagingException("Falló el envío: " + e.getMessage(), e);
        }
    }
}