package com.cinerama.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;
/**
 * Configuration component responsible for obtaining the PayPal access token.
 * Uses client credentials to authenticate with PayPal's OAuth2 endpoint.
 */
@Component
public class PayPalConfig {

    // Logger for debugging PayPal OAuth communication
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PayPalConfig.class);

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.api-url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;
    /**
     * Requests an access token from PayPal using client credentials.
     *
     * @return a valid PayPal access token as a String
     * @throws IllegalStateException if the token is missing in the response
     */
    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl + "/v1/oauth2/token",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("access_token")) {
            return (String) body.get("access_token");
        } else {
            throw new IllegalStateException("No se recibió el token de acceso desde PayPal.");
        }
    }
}
