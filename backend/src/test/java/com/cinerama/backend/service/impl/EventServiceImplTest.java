package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.EventRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import static org.mockito.Mockito.*;

public class EventServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine thymeleaf;

    @InjectMocks
    private EventServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldProcessEventSuccessfully() throws MessagingException {
        // Arrange
        EventRequest request = new EventRequest();
        //request.setContactEmail("test@example.com");
        request.setContactName("John");
        request.setEventType("Evento VIP");
        request.setCinema("Cinerama Miraflores");
        request.setDate("2025-08-01");
        request.setTime("20:00");
        request.setAttendees(50);

        // Mock Thymeleaf response
        when(thymeleaf.process(eq("event-summary-email.html"), any(Context.class)))
                .thenReturn("<html>Email content</html>");

        // Mock mail sender behavior
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        service.processEvent(request);

        // Assert: verificar que el HTML fue generado y enviado
        verify(thymeleaf).process(eq("event-summary-email.html"), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void shouldThrowMessagingException() throws Exception {
        // Arrange
        EventRequest request = new EventRequest();
        request.setContactEmail("fail@example.com");
        request.setContactName("Fail Tester");

        when(thymeleaf.process(anyString(), any())).thenReturn("<html>error</html>");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Simular error al enviar
        doThrow(new MessagingException("SMTP failure")).when(mailSender).send(mimeMessage);

        // Act & Assert
        try {
            service.processEvent(request);
            assert false : "MessagingException expected";
        } catch (MessagingException ex) {
            assert ex.getMessage().equals("SMTP failure");
        }

        verify(mailSender).send(mimeMessage);
    }
}
