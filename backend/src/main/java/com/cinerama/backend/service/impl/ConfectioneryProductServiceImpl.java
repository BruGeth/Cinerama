package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryProductRequest;
import com.cinerama.backend.dto.ConfectioneryProductResponse;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.entity.ConfectioneryCategory;
import com.cinerama.backend.repository.ConfectioneryProductRepository;
import com.cinerama.backend.repository.ConfectioneryCategoryRepository;
import com.cinerama.backend.service.ConfectioneryProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfectioneryProductServiceImpl implements ConfectioneryProductService {

    private final ConfectioneryProductRepository productRepository;
    private final ConfectioneryCategoryRepository categoryRepository;

    public ConfectioneryProductServiceImpl(ConfectioneryProductRepository productRepository, ConfectioneryCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ConfectioneryProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConfectioneryProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(this::toResponse);
    }

    @Override
    public ConfectioneryProductResponse createProduct(ConfectioneryProductRequest request) {
        ConfectioneryCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.getCategoryId()));
        ConfectioneryProduct product = new ConfectioneryProduct();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setCategory(category);
        ConfectioneryProduct saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Override
    public ConfectioneryProductResponse updateProduct(Long id, ConfectioneryProductRequest request) {
        ConfectioneryProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        ConfectioneryCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.getCategoryId()));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setCategory(category);
        ConfectioneryProduct updated = productRepository.save(product);
        return toResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ConfectioneryProductResponse toResponse(ConfectioneryProduct product) {
        return ConfectioneryProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .category(
                        ConfectioneryCategoryResponse.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .build()
                )
                .build();
    }
}