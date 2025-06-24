package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryProductRequest;
import com.cinerama.backend.dto.ConfectioneryProductResponse;
import com.cinerama.backend.service.ConfectioneryProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/confectionery-products")
public class ConfectioneryProductController {

    private final ConfectioneryProductService service;

    public ConfectioneryProductController(ConfectioneryProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConfectioneryProductResponse> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfectioneryProductResponse> getProductById(@PathVariable Long id) {
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfectioneryProductResponse> createProduct(@Valid @RequestBody ConfectioneryProductRequest request) {
        ConfectioneryProductResponse created = service.createProduct(request);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConfectioneryProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ConfectioneryProductRequest request) {
        ConfectioneryProductResponse updated = service.updateProduct(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}