package com.cinerama.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Service responsible for capturing PayPal orders.
 * Sends a request to the PayPal API to finalize payment for a given order ID.
 */
@Service
public class PayPalCaptureService {

    private static final Logger logger = LoggerFactory.getLogger(PayPalCaptureService.class);

    private final com.cinerama.backend.service.PayPalAuthService authService;

    /**
     * Constructor that injects the PayPal authentication service.
     *
     * @param authService service to retrieve PayPal access tokens
     */
    public PayPalCaptureService(com.cinerama.backend.service.PayPalAuthService authService) {
        this.authService = authService;
    }

    /**
     * Captures a PayPal order using its ID by sending a POST request
     * to the PayPal capture endpoint.
     *
     * @param orderId the ID of the PayPal order to capture
     * @return the raw JSON response as a String, or an error message
     */
    public String captureOrder(String orderId) {


        String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId + "/capture";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());// Authorization header
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();// Return the success response from PayPal
            } else {
                logger.debug("Error al capturar pago para Order ID {}: {}", orderId, response.getStatusCode());
                return "Error al capturar pago: " + response.getStatusCode();
            }
        } catch (Exception e) {
            logger.error("Error en captura de pago para Order ID {}: {}", orderId, e.getMessage());
            logger.error("Detalles de la excepción:", e);
            return "Error en captura de pago: " + e.getMessage();
        }

    }
}