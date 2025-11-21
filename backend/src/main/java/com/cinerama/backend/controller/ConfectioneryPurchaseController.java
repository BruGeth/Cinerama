package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ConfectioneryPurchaseRequest;
import com.cinerama.backend.dto.ConfectioneryPurchaseItemRequest;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.entity.Order;
import com.cinerama.backend.entity.ConfectioneryOrderItem;
import com.cinerama.backend.repository.ConfectioneryProductRepository;
import com.cinerama.backend.repository.OrderRepository;
import com.cinerama.backend.repository.ConfectioneryOrderItemRepository;
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
import com.cinerama.backend.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

/**
 * Controller for handling confectionery purchases, including direct and PayPal
 * flows.
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
    private final ConfectioneryOrderItemRepository confectioneryOrderItemRepository;
    private final PdfService pdfService;

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
            List<ConfectioneryOrderItem> orderItems = new ArrayList<>();
            for (ConfectioneryPurchaseItemRequest item : request.getItems()) {
                ConfectioneryProduct product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                if (product.getStock() < item.getQuantity()) {
                    return ResponseEntity.badRequest().body("Insufficient stock for: " + product.getName());
                }
                totalAmountPEN += product.getPrice() * item.getQuantity();
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
                // Create the confectionery item for the order
                ConfectioneryOrderItem orderItem = ConfectioneryOrderItem.builder()
                        .product(product)
                        .productName(product.getName())
                        .quantity(item.getQuantity())
                        .unitPrice(product.getPrice())
                        .totalPrice(product.getPrice() * item.getQuantity())
                        .build();
                orderItems.add(orderItem);
            }

            // Convert total to USD using shared service method
            double totalAmountUSD = paymentService.convertSolesToDollars(totalAmountPEN);

            // Get buyer's email from security context (JWT principal)
            String payerEmail = null;
            String payerName = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                payerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            }
            // Use the buyer name from the request, or default if not provided
            payerName = request.getBuyerName() != null ? request.getBuyerName() : "Usuario Cinerama";

            // Create a paid order and associate the confectionery items
            Order order = new Order();
            order.setTimestamp(LocalDateTime.now());
            order.setPayerEmail(payerEmail);
            order.setPayerName(payerName);
            order.setConfectioneryItems(orderItems);
            for (ConfectioneryOrderItem orderItem : orderItems) {
                orderItem.setOrder(order);
            }
            orderRepository.save(order);
            // Save the confectionery items
            confectioneryOrderItemRepository.saveAll(orderItems);

            return ResponseEntity.ok(Map.of(
                    "message", "Purchase completed successfully",
                    "orderId", order.getId(),
                    "totalPEN", totalAmountPEN,
                    "totalUSD", totalAmountUSD));
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
            List<ConfectioneryOrderItem> orderItems = new ArrayList<>();
            for (ConfectioneryPurchaseItemRequest item : request.getItems()) {
                ConfectioneryProduct product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                if (product.getStock() < item.getQuantity()) {
                    return ResponseEntity.badRequest().body("Insufficient stock for: " + product.getName());
                }
                totalAmountPEN += product.getPrice() * item.getQuantity();
                // Create the confectionery item for the order
                ConfectioneryOrderItem orderItem = ConfectioneryOrderItem.builder()
                        .product(product)
                        .productName(product.getName())
                        .quantity(item.getQuantity())
                        .unitPrice(product.getPrice())
                        .totalPrice(product.getPrice() * item.getQuantity())
                        .build();
                orderItems.add(orderItem);
            }

            // Convert total to USD for PayPal using shared service method
            double totalAmountUSD = paymentService.convertSolesToDollars(totalAmountPEN);

            // Create a local pending order in USD and associate the confectionery items
            Order order = new Order();
            // NO asignar currency aquí, se asigna al capturar el pago con PayPal
            order.setTimestamp(LocalDateTime.now());
            order.setAmount(BigDecimal.valueOf(totalAmountUSD));
            order.setConfectioneryItems(orderItems);
            for (ConfectioneryOrderItem orderItem : orderItems) {
                orderItem.setOrder(order);
            }
            orderRepository.save(order);
            confectioneryOrderItemRepository.saveAll(orderItems);

            // Create PayPal order via payment service
            String returnUrl = request.getReturnUrl();
            String cancelUrl = request.getCancelUrl();
            ResponseEntity<?> paymentResponse = paymentService.createPayment(totalAmountUSD, "USD", returnUrl,
                    cancelUrl);
            Map<String, String> result = null;
            Object body = paymentResponse.getBody();
            if (body instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, String> stringMap = (Map<String, String>) map;
                result = stringMap;
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
     * - Checks if the order is already marked as paid to prevent duplicate
     * processing.
     * - If not paid, captures the PayPal payment via the payment service.
     * - Updates the local order status to "PAGADA" (paid).
     * - Deducts stock for each product in the order.
     * - Returns a success message or error as appropriate.
     */
    @PostMapping("/confectionery-purchase/paypal/complete")
    public ResponseEntity<?> completePayPalPurchase(
            @RequestBody com.cinerama.backend.dto.PayPalCompleteRequest request) {
        try {
            String paymentId = request.getPaymentId();
            String payerId = request.getPayerId();

            // First, check if the order is already paid (prevents duplicate processing)
            Order order = orderRepository.findByPaypalOrderId(paymentId)
                    .orElseThrow(() -> new RuntimeException("Order not found for paymentId: " + paymentId));

            if ("COMPLETED".equalsIgnoreCase(order.getStatus())) {
                // If already paid, return success (idempotent)
                return ResponseEntity.ok(Map.of(
                        "message", "Purchase was already processed",
                        "paymentId", paymentId,
                        "status", "already_completed"));
            }

            // If not paid, attempt to capture the payment
            ResponseEntity<?> captureResponse = paymentService.capturePayment(paymentId, payerId);
            if (captureResponse.getStatusCode().is2xxSuccessful()) {
                //Do NOT update status here; the service has already done so
                //Only update buyer data if necessary
                String payerEmail = null;
                String payerName = null;
                Object captureBody = captureResponse.getBody();
                if (captureBody instanceof Map) {
                    Map<?, ?> responseMap = (Map<?, ?>) captureBody;
                    // Check if this is a duplicate order response
                    Object statusObj = responseMap.get("status");
                    Object codeObj = responseMap.get("code");

                    if ("already_completed".equals(statusObj) && "DUPLICATE_ORDER".equals(codeObj)) {
                        System.out.println("🔍 Debug: Detected duplicate order - using existing payer data");
                        // For duplicate orders, PayPal doesn't send payer data, so we keep existing
                        // data
                        payerEmail = order.getPayerEmail(); // Keep existing email
                        payerName = order.getPayerName(); // Keep existing name
                    } else {
                        // Get email
                        Object emailObj = responseMap.get("payer");
                        if (emailObj != null) {
                            payerEmail = emailObj.toString();
                        }

                        // Get name from PayPal response
                        Object nameObj = responseMap.get("payer_name");
                        if (nameObj != null) {
                            payerName = nameObj.toString();
                        } else {
                            // Fallback: try to get name from other fields
                            Object firstNameObj = responseMap.get("payer_first_name");
                            Object lastNameObj = responseMap.get("payer_last_name");
                            if (firstNameObj != null || lastNameObj != null) {
                                String firstName = firstNameObj != null ? firstNameObj.toString() : "";
                                String lastName = lastNameObj != null ? lastNameObj.toString() : "";
                                payerName = (firstName + " " + lastName).trim();
                            }
                        }

                        // Try other possible field names
                        for (Object key : responseMap.keySet()) {
                            if (key.toString().toLowerCase().contains("name")) {
                                System.out.println(
                                        "🔍 Debug: Found name-related field: " + key + " = " + responseMap.get(key));
                            }
                        }
                    }
                }

                // Only update if we have new data or if it's a duplicate order (keep existing
                // data)
                if (payerEmail != null) {
                    order.setPayerEmail(payerEmail);
                }
                if (payerName != null) {
                    order.setPayerName(payerName);
                }
                orderRepository.save(order);
                // Deduct stock for each confectionery product in the order
                for (ConfectioneryOrderItem item : order.getConfectioneryItems()) {
                    ConfectioneryProduct product = productRepository.findById(item.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProduct().getId()));
                    product.setStock(product.getStock() - item.getQuantity());
                    productRepository.save(product);
                }
                return ResponseEntity.ok(Map.of(
                        "message", "Purchase completed successfully",
                        "orderId", order.getId(),
                        "paymentId", paymentId,
                        "status", "completed"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Error processing payment"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Retrieves order details for PDF generation.
     * Returns order information including purchased items, user details, and
     * totals.
     * This endpoint is used by the frontend to generate purchase receipts.
     */
    @GetMapping("/confectionery-order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            System.out.println("🔍 Debug: Fetching order with ID: " + orderId);

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            System.out.println("🔍 Debug: Order found: " + order.getId());

            // Get confectionery items for this order
            List<ConfectioneryOrderItem> items = confectioneryOrderItemRepository.findByOrderId(orderId);

            System.out.println("🔍 Debug: Found " + items.size() + " confectionery items");

            // Calculate totals
            double totalPEN = items.stream()
                    .mapToDouble(item -> item.getTotalPrice())
                    .sum();

            double totalUSD = paymentService.convertSolesToDollars(totalPEN);
            Map<String, Object> orderDetails = Map.of(
                    "orderId", order.getId(),
                    "timestamp", order.getTimestamp(),
                    "status", order.getStatus(),
                    "payerEmail", order.getPayerEmail() != null ? order.getPayerEmail() : "",
                    "payerName", order.getPayerName() != null ? order.getPayerName() : "",
                    "items", items.stream().map(item -> Map.of(
                            "productName", item.getProductName(),
                            "quantity", item.getQuantity(),
                            "unitPrice", item.getUnitPrice(),
                            "totalPrice", item.getTotalPrice())).collect(java.util.stream.Collectors.toList()),
                    "totalPEN", totalPEN,
                    "totalUSD", totalUSD);

            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/confectionery-order/{orderId}/pdf")
    public ResponseEntity<byte[]> downloadOrderPdf(@PathVariable Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            byte[] pdfBytes = pdfService.generateConfectioneryOrderPdf(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "recibo-cinerama-" + orderId + ".pdf");

            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}