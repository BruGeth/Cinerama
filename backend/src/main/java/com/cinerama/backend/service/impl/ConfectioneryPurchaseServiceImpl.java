package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryPurchaseItemDTO;
import com.cinerama.backend.entity.*;
import com.cinerama.backend.exception.repository.ConfectioneryProductRepository;
import com.cinerama.backend.exception.repository.ConfectioneryPurchaseRepository;
import com.cinerama.backend.service.ConfectioneryPurchaseService;
import com.cinerama.backend.service.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfectioneryPurchaseServiceImpl implements ConfectioneryPurchaseService {

    private final ConfectioneryProductRepository productRepository;
    private final ConfectioneryPurchaseRepository purchaseRepository;
    private final EmailService emailService;

    public ConfectioneryPurchaseServiceImpl(
            ConfectioneryProductRepository productRepository,
            ConfectioneryPurchaseRepository purchaseRepository,
            EmailService emailService
    ) {
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.emailService = emailService;
    }

    @Override
    public ConfectioneryPurchase registerPurchase(List<ConfectioneryPurchaseItemDTO> items) {
        double[] totalHolder = {0.0};
        List<ConfectioneryPurchaseItem> itemEntities = items.stream().map(itemDto -> {
            ConfectioneryProduct product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: ID " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para: " + product.getName());
            }

            double subtotal = product.getPrice() * itemDto.getQuantity();
            totalHolder[0] += subtotal;

            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            return ConfectioneryPurchaseItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .subtotal(subtotal)
                    .build();
        }).collect(Collectors.toList());

        Double total = totalHolder[0];
        ConfectioneryPurchase purchase = ConfectioneryPurchase.builder()
                .date(LocalDateTime.now())
                .total(total)
                .items(itemEntities)
                .build();

        itemEntities.forEach(item -> item.setPurchase(purchase));
        ConfectioneryPurchase savedPurchase = purchaseRepository.save(purchase);

        // 📧 Envío de ticket por correo (simulado)
        String simulatedEmail = "cliente@ejemplo.com"; // ← reemplaza por el email real del usuario si lo tienes
        emailService.sendPurchaseConfirmation(simulatedEmail, savedPurchase);

        return savedPurchase;
    }
}
