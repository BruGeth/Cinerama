package com.cinerama.backend.config;

import com.cinerama.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration class for Spring Security.
 * Defines security settings for the Cinerama application including JWT authentication,
 * CORS configuration, and endpoint authorization rules.
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Configures the security filter chain for the application.
     *
     * Security decisions:
     * - CSRF is disabled because we use stateless JWT tokens
     * - Form login is disabled in favor of JWT-based authentication
     * - All authentication endpoints are public to allow user registration/login
     *
     * @param http the {@link HttpSecurity} object used to configure security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST APIs since we use JWT tokens
                // These are stateless and don't require CSRF protection
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // Public endpoints for user authentication (register, login, etc.)
                        .requestMatchers("/api/auth/**").permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter before username/password authentication
                // This ensures JWT tokens are processed first
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable form-based and HTTP Basic authentication
                // We only use JWT for this REST API
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Configures CORS settings for the application.
     *
     * Current configuration allows frontend development on localhost:3000.
     * TODO: Move allowed origins to application properties for environment-specific configuration
     *
     * @return the {@link CorsConfigurationSource} with the defined CORS settings
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // TODO: Extract to application.yml for different environments
        // Development: localhost:3000, Production: actual domain
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Allow credentials for JWT token transmission
        configuration.setAllowCredentials(true);

        // Cache preflight requests for 1 hour to improve performance
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}