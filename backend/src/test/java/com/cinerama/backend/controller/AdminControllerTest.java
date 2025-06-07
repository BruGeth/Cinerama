package com.cinerama.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Add test methods here to test the AdminController endpoints
    @Test
    void shouldDenyAccessToUserWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccessToUserWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                        .with(user("admin").roles("ADMIN")))

                .andExpect(status().isOk());
    }
}
