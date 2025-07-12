package com.cinerama.backend.controller;

import com.cinerama.backend.dto.EventRequest;
import com.cinerama.backend.entity.Event;
import com.cinerama.backend.exception.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {EventControllerTest.TestMailConfig.class})
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void shouldCreateEventAndReturn201() throws Exception {
        EventRequest request = new EventRequest();
        request.setEventType("Proyección privada");
        request.setCinema("Cinerama Lima");
        request.setDate("2025-08-01");
        request.setTime("18:30");
        request.setDuration("2h");
        request.setAttendees(25);
        request.setRequirements("Proyector 4K");
        request.setContactName("Ana Pérez");
        request.setContactEmail("ana@example.com");
        request.setContactPhone("987654321");
        request.setCompany("TechCorp");
        request.setMessage("Evento interno");

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("E-mail sent successfully"));

        Event saved = eventRepository.findAll().stream()
                .filter(e -> e.getContactEmail().equals("ana@example.com"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Evento no guardado en DB"));

        assert saved.getEventType().equals("Proyección privada");
    }

    @Test
    void shouldReturn500WhenMailFails() throws Exception {
        EventRequest request = new EventRequest();
        request.setEventType("Lanzamiento");
        request.setCinema("Cinerama Arequipa");
        request.setDate("2025-08-20");
        request.setTime("20:00");
        request.setAttendees(30);
        request.setContactName("Carlos Díaz");
        request.setContactEmail("fail@example.com");
        request.setContactPhone("123456789");

        doThrow(new MessagingException("Simulated SMTP error"))
                .when(mailSender).send(any(MimeMessage.class));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Simulated SMTP error"));
    }


    @Configuration
    static class TestMailConfig {
        @Bean
        public JavaMailSender mailSender() {
            return mock(JavaMailSender.class);
        }
    }
}