package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.repository.ConfectioneryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfectioneryCategoryServiceImplTest {

    private ConfectioneryCategoryRepository mockRepo;
    private ConfectioneryCategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        mockRepo = mock(ConfectioneryCategoryRepository.class);
        service = new ConfectioneryCategoryServiceImpl(mockRepo);
    }

    @Test
    void getAllCategories_returnsAllCategoriesResponse() {
        // Arranging the mock category
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setId(1L);
        category.setName("Dulces");
        when(mockRepo.findAll()).thenReturn(List.of(category));

        // Act
        List<ConfectioneryCategoryResponse> result = service.getAllCategories();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Dulces", result.get(0).getName());
        verify(mockRepo, times(1)).findAll();
    }
}