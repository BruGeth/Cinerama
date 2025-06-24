package com.cinerama.backend.service;

import com.cinerama.backend.dto.ConfectioneryCategoryRequest;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;

import java.util.List;
import java.util.Optional;

public interface ConfectioneryCategoryService {
    List<ConfectioneryCategoryResponse> getAllCategories();
    Optional<ConfectioneryCategoryResponse> getCategoryById(Long id);
    ConfectioneryCategoryResponse createCategory(ConfectioneryCategoryRequest request);
    ConfectioneryCategoryResponse updateCategory(Long id, ConfectioneryCategoryRequest request);
    void deleteCategory(Long id);
}