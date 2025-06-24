package com.cinerama.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
 * General application configuration class.
 * Defines shared beans that can be injected across the application.
 */
@Configuration
public class AppConfig {
    /**
     * Provides a RestTemplate bean to perform HTTP requests.
     * Commonly used for calling external APIs like PayPal.
     *
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
