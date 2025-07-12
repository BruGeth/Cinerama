package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryProductRequest;
import com.cinerama.backend.dto.ConfectioneryProductResponse;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.exception.repository.ConfectioneryCategoryRepository;
import com.cinerama.backend.exception.repository.ConfectioneryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfectioneryProductServiceImplTest {

    private ConfectioneryProductRepository mockProductRepository;
    private ConfectioneryCategoryRepository mockCategoryRepository;
    private ConfectioneryProductServiceImpl service;

    @BeforeEach
    void setUp() {
        mockProductRepository = mock(ConfectioneryProductRepository.class);
        mockCategoryRepository = mock(ConfectioneryCategoryRepository.class);
        service = new ConfectioneryProductServiceImpl(mockProductRepository, mockCategoryRepository);
    }

    @Test
    void getAllProducts_shouldReturnAllProductResponses() {
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

        when(mockProductRepository.findAll()).thenReturn(List.of(product));

        List<ConfectioneryProductResponse> result = service.getAllProducts();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Barra de Chocolate", result.get(0).getName());
        verify(mockProductRepository, times(1)).findAll();
    }

    @Test
    void getProductById_shouldReturnProductResponse() {
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setId(1L);
        category.setName("Dulces");

        ConfectioneryProduct product = new ConfectioneryProduct();
        product.setId(2L);
        product.setName("Caramelo");
        product.setCategory(category);

        when(mockProductRepository.findById(2L)).thenReturn(Optional.of(product));

        Optional<ConfectioneryProductResponse> result = service.getProductById(2L);

        assertTrue(result.isPresent());
        assertEquals("Caramelo", result.get().getName());
        verify(mockProductRepository).findById(2L);
    }

    @Test
    void createProduct_shouldSaveAndReturnResponse() {
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setId(3L);
        category.setName("Galletas");

        ConfectioneryProductRequest request = ConfectioneryProductRequest.builder()
                .name("Galleta Oreo")
                .description("Galleta rellena")
                .price(1.0)
                .image("oreo.jpg")
                .stock(50)
                .stockUnit("unidad")
                .categoryId(3L)
                .build();

        when(mockCategoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(mockProductRepository.save(any(ConfectioneryProduct.class))).thenAnswer(invocation -> {
            ConfectioneryProduct p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });

        ConfectioneryProductResponse response = service.createProduct(request);

        assertEquals("Galleta Oreo", response.getName());
        assertEquals(10L, response.getId());
        assertEquals("Galletas", response.getCategory().getName());
        verify(mockCategoryRepository).findById(3L);
        verify(mockProductRepository).save(any(ConfectioneryProduct.class));
    }

    @Test
    void updateProduct_shouldUpdateAndReturnResponse() {
        ConfectioneryCategory oldCategory = new ConfectioneryCategory();
        oldCategory.setId(1L);
        oldCategory.setName("Vieja");

        ConfectioneryCategory newCategory = new ConfectioneryCategory();
        newCategory.setId(2L);
        newCategory.setName("Nueva");

        ConfectioneryProduct existing = new ConfectioneryProduct();
        existing.setId(5L);
        existing.setName("Producto");
        existing.setCategory(oldCategory);

        ConfectioneryProductRequest request = ConfectioneryProductRequest.builder()
                .name("Producto Actualizado")
                .description("Desc")
                .price(3.0)
                .image("img.jpg")
                .stock(20)
                .stockUnit("unidad")
                .categoryId(2L)
                .build();

        when(mockProductRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(mockCategoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(mockProductRepository.save(any(ConfectioneryProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConfectioneryProductResponse response = service.updateProduct(5L, request);

        assertEquals("Producto Actualizado", response.getName());
        assertEquals("Nueva", response.getCategory().getName());
        verify(mockProductRepository).findById(5L);
        verify(mockCategoryRepository).findById(2L);
        verify(mockProductRepository).save(any(ConfectioneryProduct.class));
    }

    @Test
    void deleteProduct_shouldCallRepositoryDelete() {
        service.deleteProduct(7L);
        verify(mockProductRepository).deleteById(7L);
    }
}