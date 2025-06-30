package com.cinerama.backend.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

/**
 * Service responsible for authenticating with PayPal using client credentials.
 * Retrieves an access token used to authorize other PayPal API requests.
 */
@Service
public class PayPalAuthService {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    private static final Logger logger = LoggerFactory.getLogger(PayPalAuthService.class);

    /**
     * Logs PayPal client configuration upon service initialization.
     * Useful for verifying that configuration properties have been loaded correctly.
     */
    @PostConstruct
    public void checkConfig() {
        logger.info("Client ID desde application.yml: {}", clientId);
        logger.info("Client Secret desde application.yml: {}", clientSecret);
    }

    /**
     * Requests a new access token from PayPal's OAuth2 API using client credentials.
     *
     * @return raw JSON response containing the access token, or an error message
     */
    public String getAccessToken() {
        String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);// Set Basic Authentication header
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();// Should be a JSON string with access_token
            } else {
                System.err.println("Error en autenticación PayPal: " + response.getStatusCode());
                return "Error: No se pudo obtener el token de acceso.";
            }
        } catch (Exception e) {
            System.err.println("Excepción en PayPalAuthService: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
