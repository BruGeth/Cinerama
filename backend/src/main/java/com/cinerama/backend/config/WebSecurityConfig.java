package com.cinerama.backend.config;

import com.cinerama.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
 *
 * <p>Defines security settings for the Cinerama cinema booking application including
 * JWT authentication, CORS configuration, and endpoint authorization rules.</p>
 *
 * <h2>Security Architecture:</h2>
 * <ul>
 *   <li>JWT-based stateless authentication (no sessions)</li>
 *   <li>CSRF protection disabled for REST API</li>
 *   <li>CORS enabled for frontend integration</li>
 *   <li>Public access to authentication endpoints</li>
 *   <li>Protected access to all other endpoints</li>
 * </ul>
 *
 * <h2>Authentication Flow:</h2>
 * <ol>
 *   <li>User sends login credentials to /api/auth/login</li>
 *   <li>Server validates credentials and returns JWT token</li>
 *   <li>Client includes JWT token in Authorization header</li>
 *   <li>JwtAuthenticationFilter validates token on each request</li>
 * </ol>
 *
 * @author Cinerama Development Team
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Configures the security filter chain for the application.
     *
     * <p>This method sets up the complete security configuration including
     * authentication filters, authorization rules, and security features.</p>
     *
     * <h3>Security Decisions:</h3>
     * <ul>
     *   <li>CSRF is disabled because we use stateless JWT tokens</li>
     *   <li>Form login is disabled in favor of JWT-based authentication</li>
     *   <li>HTTP Basic authentication is disabled for better security</li>
     *   <li>All authentication endpoints are public to allow user registration/login</li>
     *   <li>All other endpoints require valid JWT authentication</li>
     * </ul>
     *
     * <h3>Filter Chain Order:</h3>
     * <ol>
     *   <li>CORS filter (built-in Spring Security)</li>
     *   <li>JWT Authentication Filter (custom)</li>
     *   <li>Username/Password Authentication Filter (Spring Security)</li>
     * </ol>
     *
     * <h3>Endpoint Access Rules:</h3>
     * <ul>
     *   <li><strong>/api/auth/**</strong> - Public access (register, login, verify) - To See</li>
     *   <li><strong>/api/seats/available**</strong> - Public access to available seats</li>
     *   <li><strong>/api/user/register</strong> - Public access for user registration - To See</li>
     *   <li><strong>GET requests to /api/genres/**, /api/movies/**, /api/showtimes/**, /api/confectionery-categories/**, /api/confectionery-products/**, /api/orders/**</strong> - Public access for fetching data</li>
     *   <li><strong>/api/confectionery-purchase</strong> - Public access for confectionery purchase - To See</li>
     *   <li><strong>All other endpoints</strong> - Requires valid JWT token</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object used to configure security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     *
     * @see JwtAuthenticationFilter for JWT token validation logic
     * @see SecurityFilterChain for Spring Security filter chain documentation
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST APIs since we use JWT tokens
                // These are stateless and don't require CSRF protection
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS with custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure endpoint authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints for authentication, registration, and some resources
                        .requestMatchers("/api/auth/**", "/api/seats/available**", "/api/user/register"
                                         ,"/api/events","/api/festarama","/api/specialfunctions","/api/advertising" ).permitAll()
                        // Public GET endpoints for genres, movies, showtimes, confectionery, and orders
                        .requestMatchers(HttpMethod.GET, "/api/genres/**",
                                              "/api/movies/**","/api/showtimes/**",
                                                "/api/confectionery-categories/**",
                                 "/api/confectionery-products/**","/api/orders/**").permitAll()
                        // Public endpoints for payment and confectionery purchase (including PayPal integration)
                        // Added to allow PayPal payment flow and confectionery purchases without authentication
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/confectionery-purchase", "/api/confectionery-purchase/**").permitAll()
                        // User profile, tickets, and seats require authentication
                        .requestMatchers("/api/user/", "/api/tickets/**", "/api/seats/**").authenticated()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter before username/password authentication
                // This ensures JWT tokens are processed first in the filter chain
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable form-based and HTTP Basic authentication
                // We only use JWT for this REST API
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings for the application.
     *
     * <p>This configuration allows the frontend application to make requests to the
     * backend API from different origins, which is essential for modern web applications
     * where frontend and backend are served from different domains or ports.</p>
     *
     * <h3>Current CORS Configuration:</h3>
     * <ul>
     *   <li><strong>Allowed Origins:</strong> http://localhost:3000 (React development server)</li>
     *   <li><strong>Allowed Methods:</strong> GET, POST, PUT, DELETE, OPTIONS</li>
     *   <li><strong>Allowed Headers:</strong> Authorization, Content-Type</li>
     *   <li><strong>Credentials:</strong> Enabled for JWT token transmission</li>
     *   <li><strong>Preflight Cache:</strong> 1 hour (3600 seconds)</li>
     * </ul>
     *
     * <h3>Environment Considerations:</h3>
     * <ul>
     *   <li><strong>Development:</strong> localhost:3000 for React development</li>
     *   <li><strong>Production:</strong> Should be configured with actual domain</li>
     *   <li><strong>Staging:</strong> Should use staging domain</li>
     * </ul>
     *
     * <p><strong>Security Note:</strong> In production, allowed origins should be
     * restricted to only trusted domains to prevent unauthorized cross-origin requests.</p>
     *
     * @return the {@link CorsConfigurationSource} with the defined CORS settings
     *
     * @todo Move allowed origins to application properties for environment-specific configuration:
     * <ul>
     *   <li>Development: localhost:3000</li>
     *   <li>Production: actual production domain</li>
     *   <li>Staging: staging environment domain</li>
     * </ul>
     *
     * @see CorsConfiguration for detailed CORS configuration options
     * @see UrlBasedCorsConfigurationSource for URL-based CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configure allowed origins for different environments
        // TODO: Extract to application.yml for different environments
        // Development: localhost:3000, Production: actual domain
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        // Configure allowed HTTP methods for API operations
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Configure allowed headers for JWT authentication and content negotiation
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Allow credentials for JWT token transmission in Authorization header
        configuration.setAllowCredentials(true);

        // Cache preflight requests for 1 hour to improve performance
        // Reduces OPTIONS requests from browser for same-origin policy
        configuration.setMaxAge(3600L);

        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
