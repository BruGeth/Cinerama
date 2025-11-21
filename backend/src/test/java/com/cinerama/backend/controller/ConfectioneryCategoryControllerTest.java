package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.dto.ConfectioneryCategoryRequest;
import com.cinerama.backend.service.ConfectioneryCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConfectioneryCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfectioneryCategoryService service;

    @Test
    void shouldReturnAllCategories() throws Exception {
        ConfectioneryCategoryResponse category1 = new ConfectioneryCategoryResponse(1L, "Dulces");
        ConfectioneryCategoryResponse category2 = new ConfectioneryCategoryResponse(2L, "Bebidas");
        when(service.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/api/confectionery-categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Dulces"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Bebidas"));
    }

    @Test
    void shouldReturnCategoryById() throws Exception {
        ConfectioneryCategoryResponse category = new ConfectioneryCategoryResponse(1L, "Dulces");
        when(service.getCategoryById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/confectionery-categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Dulces"));
    }

    @Test
    void shouldAllowAdminToCreateCategory() throws Exception {
        ConfectioneryCategoryResponse created = new ConfectioneryCategoryResponse(3L, "Snacks");
        when(service.createCategory(new ConfectioneryCategoryRequest("Snacks"))).thenReturn(created);

        mockMvc.perform(post("/api/confectionery-categories")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Snacks\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Snacks"));
    }

    @Test
    void shouldDenyUserToCreateCategory() throws Exception {
        mockMvc.perform(post("/api/confectionery-categories")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Snacks\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToUpdateCategory() throws Exception {
        ConfectioneryCategoryResponse updated = new ConfectioneryCategoryResponse(1L, "Galletas");
        when(service.updateCategory(1L, new ConfectioneryCategoryRequest("Galletas"))).thenReturn(updated);

        mockMvc.perform(put("/api/confectionery-categories/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Galletas\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Galletas"));
    }

    @Test
    void shouldDenyUserToUpdateCategory() throws Exception {
        mockMvc.perform(put("/api/confectionery-categories/1")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Galletas\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToDeleteCategory() throws Exception {
        doNothing().when(service).deleteCategory(1L);

        mockMvc.perform(delete("/api/confectionery-categories/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDenyUserToDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/confectionery-categories/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}