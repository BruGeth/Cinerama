package com.cinerama.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

/**
 * Service responsible for capturing PayPal orders.
 * Sends a request to the PayPal API to finalize payment for a given order ID.
 */
@Service
public class PayPalOrderService {

    private final PayPalAuthService authService;
    /**
     * Constructor that injects the PayPal authentication service.
     *
     * @param authService service used to retrieve the PayPal access token
     */
    @Autowired
    public PayPalOrderService(PayPalAuthService authService) {
        this.authService = authService;
    }
    /**
     * Creates a PayPal order with the provided amount and currency.
     * Validates input and communicates with the PayPal checkout API.
     *
     * @param amount   the total value of the order
     * @param currency the currency code (e.g., "USD")
     * @return raw JSON response from PayPal or an error message
     */
    public String createOrder(Double amount, String currency) {
        // Validate input
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía.");
        }

        // Obtain access token and set up request details
        String accessToken = authService.getAccessToken();
        String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders";

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct JSON body for the order creation
        String jsonBody = "{"
                + "\"intent\":\"CAPTURE\","
                + "\"purchase_units\":[{"
                + "    \"amount\":{"
                + "        \"currency_code\":\"" + currency + "\","
                + "        \"value\":\"" + amount + "\""
                + "    }"
                + "}]"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {

            // Send POST request to PayPal
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();// Return raw response from PayPal
            } else {

                return "Error en creación de orden: " + response.getStatusCode();
            }
        } catch (Exception e) {

            return "Excepción en creación de orden: " + e.getMessage();
        }
    }
}
