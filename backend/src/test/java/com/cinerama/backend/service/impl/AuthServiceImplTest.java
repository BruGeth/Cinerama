package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.util.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("SecurePass123");
        request.setConfirmPassword("SecurePass123");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = authService.register(request);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertTrue(result.getPassword().startsWith("$2a$")); // BCrypt prefix
        assertFalse(result.isEnabled()); // Not enabled yet
        assertNotNull(result.getVerificationCode());

        // Verify email was sent
        verify(emailService, times(1))
                .sendVerificationEmail(eq(result.getEmail()), anyString());
    }
}
