package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryPurchaseItemDTO;
import com.cinerama.backend.entity.ConfectioneryPurchase;
import com.cinerama.backend.service.ConfectioneryPurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/confectionery-purchase")
public class ConfectioneryPurchaseController {

    private final ConfectioneryPurchaseService purchaseService;

    public ConfectioneryPurchaseController(ConfectioneryPurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody List<ConfectioneryPurchaseItemDTO> items) {
        try {
            ConfectioneryPurchase purchase = purchaseService.registerPurchase(items);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Compra registrada correctamente");
            response.put("total", purchase.getTotal());
            response.put("date", purchase.getDate());
            response.put("items", purchase.getItems().stream().map(i -> Map.of(
                    "name", i.getProduct().getName(),
                    "price", i.getProduct().getPrice(),
                    "quantity", i.getQuantity()
            )).collect(Collectors.toList()));

            return ResponseEntity.ok(response); // ✅ Esta es la línea corregida

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
