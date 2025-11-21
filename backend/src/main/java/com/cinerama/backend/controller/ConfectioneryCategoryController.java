package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryCategoryRequest;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.service.ConfectioneryCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/confectionery-categories")
public class ConfectioneryCategoryController {

    private final ConfectioneryCategoryService categoryService;

    public ConfectioneryCategoryController(ConfectioneryCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<ConfectioneryCategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ConfectioneryCategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfectioneryCategoryResponse createCategory(@RequestBody ConfectioneryCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ConfectioneryCategoryResponse updateCategory(@PathVariable Long id, @RequestBody ConfectioneryCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}