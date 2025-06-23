package com.cinerama.backend.controller;

import com.cinerama.backend.config.PayPalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;
/**
 * REST controller responsible for managing PayPal orders.
 * Handles order creation and capture by communicating with PayPal's API.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final PayPalConfig payPalConfig;
    private final RestTemplate restTemplate;
    /**
     * Constructor to inject PayPal configuration and shared RestTemplate.
     */
    @Autowired
    public OrderController(PayPalConfig payPalConfig, RestTemplate restTemplate) {
        this.payPalConfig = payPalConfig;
        this.restTemplate = restTemplate;
    }
    /**
     * Creates a new PayPal order with the given amount.
     *
     * @param body a JSON map containing the "amount" as a string.
     * @return the generated PayPal order ID if successful, or an error message.
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, String> body) {
        try {
            String amount = body.get("amount");
            String accessToken = payPalConfig.getAccessToken();

            // Prepare headers with access token and JSON content type
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build the order payload for PayPal
            Map<String, Object> purchaseUnit = Map.of(
                    "amount", Map.of(
                            "currency_code", "USD",
                            "value", amount
                    )
            );

            Map<String, Object> orderPayload = Map.of(
                    "intent", "CAPTURE",
                    "purchase_units", List.of(purchaseUnit)
            );

            // Make the POST request to PayPal
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(orderPayload, headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://api-m.sandbox.paypal.com/v2/checkout/orders",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {
                    }
            );

            // Extract PayPal order ID from response
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("id")) {
                String payPalOrderId = (String) responseBody.get("id");
                return ResponseEntity.ok(Map.of("id", payPalOrderId));
            } else {
                throw new IllegalStateException("No se recibió el ID de orden desde PayPal.");
            }

        } catch (Exception e) {
            logger.error("Error al crear orden con PayPal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo crear la orden en PayPal"));
        }
    }
    /**
     * Captures a PayPal order that was previously created and approved.
     *
     * @param orderId the PayPal order ID to capture.
     * @return the raw PayPal response if successful, or an error message.
     */
    @PostMapping("/{orderId}/capture")
    public ResponseEntity<Map<String, Object>> captureOrder(@PathVariable String orderId) {
        try {
            String accessToken = payPalConfig.getAccessToken();

            // Set authorization and content-type headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            // Send capture request to PayPal
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId + "/capture",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            logger.error("Error al capturar orden en PayPal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo capturar la orden en PayPal"));
        }
    }
}
