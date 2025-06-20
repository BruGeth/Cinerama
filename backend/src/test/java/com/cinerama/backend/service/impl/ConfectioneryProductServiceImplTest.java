package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryProductResponse;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.repository.ConfectioneryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConfectioneryProductServiceImplTest {

    private ConfectioneryProductRepository mockRepository;
    private ConfectioneryProductServiceImpl service;

    @BeforeEach
    void setUp() {
        mockRepository = mock(ConfectioneryProductRepository.class);
        service = new ConfectioneryProductServiceImpl(mockRepository);
    }

    @Test
    void getAllProducts_shouldReturnAllProductResponses() {
        // Arrange
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setId(1L);
        category.setName("Dulces");

        ConfectioneryProduct product = new ConfectioneryProduct();
        product.setId(1L);
        product.setName("Barra de Chocolate");
        product.setDescription("Chocolate delicioso");
        product.setPrice(2.5);
        product.setImage("chocolate.jpg");
        product.setCategory(category);

        when(mockRepository.findAll()).thenReturn(List.of(product));

        // Act
        List<ConfectioneryProductResponse> result = service.getAllProducts();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Barra de Chocolate", result.get(0).getName());
        assertEquals("Chocolate delicioso", result.get(0).getDescription());
        assertEquals(2.5, result.get(0).getPrice());
        assertEquals("chocolate.jpg", result.get(0).getImage());
        assertEquals(1L, result.get(0).getCategory().getId());
        assertEquals("Dulces", result.get(0).getCategory().getName());
        verify(mockRepository, times(1)).findAll();
    }
}