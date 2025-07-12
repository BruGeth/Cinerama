package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryCategoryRequest;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.exception.repository.ConfectioneryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setId(1L);
        category.setName("Dulces");
        when(mockRepo.findAll()).thenReturn(List.of(category));

        List<ConfectioneryCategoryResponse> result = service.getAllCategories();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Dulces", result.get(0).getName());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void getCategoryById_returnsCategoryResponse() {
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setId(2L);
        category.setName("Snacks");
        when(mockRepo.findById(2L)).thenReturn(Optional.of(category));

        Optional<ConfectioneryCategoryResponse> result = service.getCategoryById(2L);

        assertTrue(result.isPresent());
        assertEquals("Snacks", result.get().getName());
        verify(mockRepo).findById(2L);
    }

    @Test
    void getCategoryById_returnsEmptyIfNotFound() {
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        Optional<ConfectioneryCategoryResponse> result = service.getCategoryById(99L);

        assertTrue(result.isEmpty());
        verify(mockRepo).findById(99L);
    }

    @Test
    void createCategory_savesAndReturnsResponse() {
        ConfectioneryCategoryRequest request = ConfectioneryCategoryRequest.builder()
                .name("Galletas")
                .build();

        ConfectioneryCategory saved = new ConfectioneryCategory();
        saved.setId(3L);
        saved.setName("Galletas");

        when(mockRepo.save(any(ConfectioneryCategory.class))).thenReturn(saved);

        ConfectioneryCategoryResponse response = service.createCategory(request);

        assertEquals(3L, response.getId());
        assertEquals("Galletas", response.getName());
        verify(mockRepo).save(any(ConfectioneryCategory.class));
    }

    @Test
    void updateCategory_updatesAndReturnsResponse() {
        ConfectioneryCategory existing = new ConfectioneryCategory();
        existing.setId(4L);
        existing.setName("Vieja");

        ConfectioneryCategoryRequest request = ConfectioneryCategoryRequest.builder()
                .name("Nueva")
                .build();

        ConfectioneryCategory updated = new ConfectioneryCategory();
        updated.setId(4L);
        updated.setName("Nueva");

        when(mockRepo.findById(4L)).thenReturn(Optional.of(existing));
        when(mockRepo.save(any(ConfectioneryCategory.class))).thenReturn(updated);

        ConfectioneryCategoryResponse response = service.updateCategory(4L, request);

        assertEquals(4L, response.getId());
        assertEquals("Nueva", response.getName());
        verify(mockRepo).findById(4L);
        verify(mockRepo).save(existing);
    }

    @Test
    void updateCategory_throwsIfNotFound() {
        ConfectioneryCategoryRequest request = ConfectioneryCategoryRequest.builder()
                .name("Nueva")
                .build();

        when(mockRepo.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateCategory(5L, request));
        assertTrue(ex.getMessage().contains("Category not found with id: 5"));
        verify(mockRepo).findById(5L);
    }

    @Test
    void deleteCategory_callsRepositoryDelete() {
        service.deleteCategory(6L);
        verify(mockRepo).deleteById(6L);
    }
}