package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryPurchaseRequest;
import com.cinerama.backend.dto.ConfectioneryPurchaseItemRequest;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.entity.Order;
import com.cinerama.backend.entity.CartItem;
import com.cinerama.backend.repository.ConfectioneryProductRepository;
import com.cinerama.backend.repository.OrderRepository;
import com.cinerama.backend.service.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controller for handling confectionery purchases, including direct and PayPal flows.
 * 
 * Key improvements:
 * - Added PayPal integration for confectionery purchases.
 * - Handles currency conversion from PEN to USD for PayPal.
 * - Ensures local orders are created and updated in USD.
 * - Prevents duplicate payment processing by checking order status.
 * - Improved DTO usage for purchase items.
 * - Robust stock validation and update logic.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfectioneryPurchaseController {

    private final ConfectioneryProductRepository productRepository;
    private final PaymentServiceImpl paymentService;
    private final OrderRepository orderRepository;

    /**
     * Handles direct confectionery purchases (non-PayPal).
     * - Validates stock for each item.
     * - Calculates total in PEN and converts to USD.
     * - Updates product stock and creates a paid order in USD.
     * - Returns both PEN and USD totals in the response.
     */
    @PostMapping("/confectionery-purchase")
    public ResponseEntity<?> purchase(@RequestBody ConfectioneryPurchaseRequest request) {
        try {
            // Validate stock and calculate total in PEN
            double totalAmountPEN = 0.0;
            for (ConfectioneryPurchaseItemRequest item : request.getItems()) {
                ConfectioneryProduct product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                if (product.getStock() < item.getQuantity()) {
                    return ResponseEntity.badRequest().body("Insufficient stock for: " + product.getName());
                }
                totalAmountPEN += product.getPrice() * item.getQuantity();
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            }

            // Convert total to USD using shared service method
            double totalAmountUSD = paymentService.convertSolesToDollars(totalAmountPEN);

            // Get buyer's email from security context (JWT principal)
            String payerEmail = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                payerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            }

            // Create a paid order in USD
            Order order = new Order();
            order.setStatus("pagada");
            order.setTimestamp(LocalDateTime.now());
            order.setCurrency("USD");
            order.setAmount(BigDecimal.valueOf(totalAmountUSD));
            order.setPayerEmail(payerEmail); // Register buyer's email for tracking and auditability
            orderRepository.save(order);

            return ResponseEntity.ok(Map.of(
                "message", "Purchase completed successfully",
                "totalPEN", totalAmountPEN,
                "totalUSD", totalAmountUSD
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Creates a PayPal order for confectionery purchase and a local pending order.
     * - Validates stock and calculates total in PEN.
     * - Converts total to USD for PayPal.
     * - Creates a local order in USD with status "PENDIENTE".
     * - Prepares cart items for the order.
     * - Calls the payment service to create a PayPal order.
     * - Stores the PayPal order ID in the local order for later reference.
     * - Returns PayPal approval URL and payment ID to the frontend.
     */
    @PostMapping("/confectionery-purchase/paypal/create")
    public ResponseEntity<?> createPayPalOrder(@RequestBody ConfectioneryPurchaseRequest request) {
        try {
            // Validate stock and calculate total in PEN
            double totalAmountPEN = 0.0;
            List<CartItem> cartItems = new ArrayList<>();
            for (ConfectioneryPurchaseItemRequest item : request.getItems()) {
                ConfectioneryProduct product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                if (product.getStock() < item.getQuantity()) {
                    return ResponseEntity.badRequest().body("Insufficient stock for: " + product.getName());
                }
                totalAmountPEN += product.getPrice() * item.getQuantity();
                CartItem cartItem = new CartItem();
                cartItem.setTitle(product.getName());
                cartItem.setQuantity(item.getQuantity());
                cartItem.setPrice(product.getPrice());
                cartItems.add(cartItem);
            }

            // Convert total to USD for PayPal using shared service method
            double totalAmountUSD = paymentService.convertSolesToDollars(totalAmountPEN);

            // Create a local pending order in USD
            Order order = new Order();
            order.setStatus("pendiente");
            order.setTimestamp(LocalDateTime.now());
            order.setCurrency("USD");
            order.setAmount(BigDecimal.valueOf(totalAmountUSD));
            order.setCart(cartItems);
            for (CartItem cartItem : cartItems) {
                cartItem.setOrder(order);
            }
            orderRepository.save(order);

            // Create PayPal order via payment service
            String returnUrl = request.getReturnUrl();
            String cancelUrl = request.getCancelUrl();
            ResponseEntity<?> paymentResponse = paymentService.createPayment(totalAmountUSD, "USD", returnUrl, cancelUrl);
            Map<String, String> result = null;
            Object body = paymentResponse.getBody();
            if (body instanceof Map) {
                result = (Map<String, String>) body;
            }
            if (result == null || !result.containsKey("payment_id")) {
                return ResponseEntity.status(500).body(Map.of("error", "Could not create PayPal order"));
            }
            String paymentId = result.get("payment_id");
            order.setPaypalOrderId(paymentId);
            orderRepository.save(order);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Completes the confectionery purchase after successful PayPal payment.
     * - Checks if the order is already marked as paid to prevent duplicate processing.
     * - If not paid, captures the PayPal payment via the payment service.
     * - Updates the local order status to "PAGADA" (paid).
     * - Deducts stock for each product in the order.
     * - Returns a success message or error as appropriate.
     */
    @PostMapping("/confectionery-purchase/paypal/complete")
    public ResponseEntity<?> completePayPalPurchase(@RequestBody com.cinerama.backend.dto.PayPalCompleteRequest request) {
        try {
            String paymentId = request.getPaymentId();
            String payerId = request.getPayerId();
            
            // First, check if the order is already paid (prevents duplicate processing)
            Order order = orderRepository.findByPaypalOrderId(paymentId)
                .orElseThrow(() -> new RuntimeException("Order not found for paymentId: " + paymentId));
            
            if ("pagada".equals(order.getStatus())) {
                // If already paid, return success (idempotent)
                return ResponseEntity.ok(Map.of(
                    "message", "Purchase was already processed",
                    "paymentId", paymentId,
                    "status", "already_completed"
                ));
            }
            
            // If not paid, attempt to capture the payment
            ResponseEntity<?> captureResponse = paymentService.capturePayment(paymentId, payerId);
            if (captureResponse.getStatusCode().is2xxSuccessful()) {
                // Update local order status to paid
                order.setStatus("pagada");
                // Get PayPal payer email from capture response
                String payerEmail = null;
                Object body = captureResponse.getBody();
                if (body instanceof Map) {
                    Object emailObj = ((Map<?, ?>) body).get("payer");
                    if (emailObj != null) {
                        payerEmail = emailObj.toString();
                    }
                }
                order.setPayerEmail(payerEmail); // Save the PayPal payer's email
                orderRepository.save(order);
                // Deduct stock for each product in the order
                for (CartItem item : order.getCart()) {
                    ConfectioneryProduct product = productRepository.findByName(item.getTitle());
                    if (product == null) {
                        throw new RuntimeException("Product not found: " + item.getTitle());
                    }
                    product.setStock(product.getStock() - item.getQuantity());
                    productRepository.save(product);
                }
                return ResponseEntity.ok(Map.of(
                    "message", "Purchase completed successfully",
                    "paymentId", paymentId,
                    "status", "completed"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Error processing payment"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}