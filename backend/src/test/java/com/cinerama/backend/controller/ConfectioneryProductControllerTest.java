package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryProductRequest;
import com.cinerama.backend.dto.ConfectioneryProductResponse;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.service.ConfectioneryProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConfectioneryProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfectioneryProductService service;

    private final ConfectioneryCategoryResponse category =
            ConfectioneryCategoryResponse.builder().id(1L).name("Dulces").build();

    @Test
    void shouldReturnAllProducts() throws Exception {
        ConfectioneryProductResponse product1 = ConfectioneryProductResponse.builder()
                .id(1L).name("Coca Cola").description("Bebida").price(2.0).image("coca.jpg")
                .stock(50).stockUnit("envase").category(category).build();
        ConfectioneryProductResponse product2 = ConfectioneryProductResponse.builder()
                .id(2L).name("Palomitas").description("Snack").price(1.5).image("popcorn.jpg")
                .stock(100).stockUnit("porción").category(category).build();
        when(service.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/confectionery-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Coca Cola"))
                .andExpect(jsonPath("$[0].description").value("Bebida"))
                .andExpect(jsonPath("$[0].price").value(2.0))
                .andExpect(jsonPath("$[0].image").value("coca.jpg"))
                .andExpect(jsonPath("$[0].stock").value(50))
                .andExpect(jsonPath("$[0].stockUnit").value("envase"))
                .andExpect(jsonPath("$[0].category.id").value(1L))
                .andExpect(jsonPath("$[0].category.name").value("Dulces"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Palomitas"))
                .andExpect(jsonPath("$[1].stock").value(100))
                .andExpect(jsonPath("$[1].stockUnit").value("porción"));
    }

    @Test
    void shouldReturnProductById() throws Exception {
        ConfectioneryProductResponse product = ConfectioneryProductResponse.builder()
                .id(1L).name("Coca Cola").description("Bebida").price(2.0).image("coca.jpg")
                .stock(50).stockUnit("envase").category(category).build();
        when(service.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/confectionery-products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Coca Cola"))
                .andExpect(jsonPath("$.description").value("Bebida"))
                .andExpect(jsonPath("$.price").value(2.0))
                .andExpect(jsonPath("$.image").value("coca.jpg"))
                .andExpect(jsonPath("$.stock").value(50))
                .andExpect(jsonPath("$.stockUnit").value("envase"))
                .andExpect(jsonPath("$.category.id").value(1L))
                .andExpect(jsonPath("$.category.name").value("Dulces"));
    }

    @Test
    void shouldAllowAdminToCreateProduct() throws Exception {
        ConfectioneryProductRequest request = new ConfectioneryProductRequest(
                "Nachos", "Snack", 3.0, "nachos.jpg", 1L, 100, "unit"
        );
        ConfectioneryProductResponse created = ConfectioneryProductResponse.builder()
                .id(3L).name("Nachos").description("Snack").price(3.0).image("nachos.jpg")
                .stock(100).stockUnit("unit").category(category).build();
        when(service.createProduct(request)).thenReturn(created);

        mockMvc.perform(post("/api/confectionery-products")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Nachos\",\"description\":\"Snack\",\"price\":3.0,\"image\":\"nachos.jpg\",\"stock\":100,\"stockUnit\":\"unit\",\"categoryId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Nachos"))
                .andExpect(jsonPath("$.description").value("Snack"))
                .andExpect(jsonPath("$.price").value(3.0))
                .andExpect(jsonPath("$.image").value("nachos.jpg"))
                .andExpect(jsonPath("$.stock").value(100))
                .andExpect(jsonPath("$.stockUnit").value("unit"))
                .andExpect(jsonPath("$.category.id").value(1L));
    }

    @Test
    void shouldDenyUserToCreateProduct() throws Exception {
        mockMvc.perform(post("/api/confectionery-products")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Nachos\",\"description\":\"Snack\",\"price\":3.0,\"image\":\"nachos.jpg\",\"stock\":100,\"stockUnit\":\"unit\",\"categoryId\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToUpdateProduct() throws Exception {
        ConfectioneryProductRequest request = new ConfectioneryProductRequest(
                "Agua", "Bebida", 1.0, "agua.jpg", 1L, 200, "envase"
        );
        ConfectioneryProductResponse updated = ConfectioneryProductResponse.builder()
                .id(1L).name("Agua").description("Bebida").price(1.0).image("agua.jpg")
                .stock(200).stockUnit("envase").category(category).build();
        when(service.updateProduct(1L, request)).thenReturn(updated);

        mockMvc.perform(put("/api/confectionery-products/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Agua\",\"description\":\"Bebida\",\"price\":1.0,\"image\":\"agua.jpg\",\"stock\":200,\"stockUnit\":\"envase\",\"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Agua"))
                .andExpect(jsonPath("$.description").value("Bebida"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.image").value("agua.jpg"))
                .andExpect(jsonPath("$.stock").value(200))
                .andExpect(jsonPath("$.stockUnit").value("envase"))
                .andExpect(jsonPath("$.category.id").value(1L));
    }

    @Test
    void shouldDenyUserToUpdateProduct() throws Exception {
        mockMvc.perform(put("/api/confectionery-products/1")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Agua\",\"description\":\"Bebida\",\"price\":1.0,\"image\":\"agua.jpg\",\"stock\":200,\"stockUnit\":\"envase\",\"categoryId\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToDeleteProduct() throws Exception {
        doNothing().when(service).deleteProduct(1L);

        mockMvc.perform(delete("/api/confectionery-products/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDenyUserToDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/confectionery-products/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}