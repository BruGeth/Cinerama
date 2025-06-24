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

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleaf;
    private final EventRepository eventRepository;

    @Override
    public void processEvent(EventRequest req) throws MessagingException {
        //Save to the database
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

        //Create HTML content with Thymeleaf
        Context ctx = new Context();
        ctx.setVariable("event", req);
        String html = thymeleaf.process("event-summary-email.html", ctx);

        //Prepare and send email
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

        helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername()); // MUY IMPORTANTE
        helper.setTo(req.getContactEmail());
        helper.setSubject("Resumen de tu evento en Cinerama"); // Sin emojis por compatibilidad
        helper.setText(html, true); // true = contenido HTML

        try {
            mailSender.send(mime);
            System.out.println("✅ Correo enviado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            throw new MessagingException("Falló el envío: " + e.getMessage(), e);
        }
    }
}