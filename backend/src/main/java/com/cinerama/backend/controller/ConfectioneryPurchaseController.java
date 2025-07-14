package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryPurchaseRequest;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.repository.ConfectioneryProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfectioneryPurchaseController {

    private final ConfectioneryProductRepository productRepository;

    @PostMapping("/confectionery-purchase")
    public ResponseEntity<?> purchase(@RequestBody ConfectioneryPurchaseRequest request) {
        for (ConfectioneryPurchaseRequest.Item item : request.getItems()) {
            ConfectioneryProduct product = productRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getId()));
            if (product.getStock() < item.getQuantity()) {
                return ResponseEntity.badRequest().body("Stock insuficiente para: " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        return ResponseEntity.ok("Compra realizada con éxito");
    }
}