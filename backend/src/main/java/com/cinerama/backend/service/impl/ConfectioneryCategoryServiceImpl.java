package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryCategoryRequest;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.exception.repository.ConfectioneryCategoryRepository;
import com.cinerama.backend.service.ConfectioneryCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfectioneryCategoryServiceImpl implements ConfectioneryCategoryService {

    private final ConfectioneryCategoryRepository repository;

    public ConfectioneryCategoryServiceImpl(ConfectioneryCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ConfectioneryCategoryResponse> getAllCategories() {
        return repository.findAll().stream()
                .map(cat -> new ConfectioneryCategoryResponse(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConfectioneryCategoryResponse> getCategoryById(Long id) {
        return repository.findById(id)
                .map(cat -> new ConfectioneryCategoryResponse(cat.getId(), cat.getName()));
    }

    @Override
    public ConfectioneryCategoryResponse createCategory(ConfectioneryCategoryRequest request) {
        ConfectioneryCategory category = new ConfectioneryCategory();
        category.setName(request.getName());
        ConfectioneryCategory saved = repository.save(category);
        return new ConfectioneryCategoryResponse(saved.getId(), saved.getName());
    }

    @Override
    public ConfectioneryCategoryResponse updateCategory(Long id, ConfectioneryCategoryRequest request) {
        ConfectioneryCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setName(request.getName());
        ConfectioneryCategory updated = repository.save(category);
        return new ConfectioneryCategoryResponse(updated.getId(), updated.getName());
    }

    @Override
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}