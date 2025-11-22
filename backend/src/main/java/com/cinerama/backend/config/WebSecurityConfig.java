package com.cinerama.backend.config;

import com.cinerama.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;

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

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private Boolean allowCredentials;

    @Value("${cors.max-age}")
    private Long maxAge;

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
                        // Public endpoint for Prometheus metrics scraping
                        .requestMatchers("/actuator/prometheus").permitAll()
                        // Public endpoints for user authentication (register, login, verify)
                        .requestMatchers("/api/auth/**", "/api/seats/available**", "/api/user/register"
                                         ,"/api/events","/api/festarama","/api/specialfunctions","/api/advertising", "/api/movies/update-status" ).permitAll()
                        // Public access to genre, movie, showtime, and confectionery category endpoints
                        .requestMatchers(HttpMethod.GET, "/api/genres/**",
                                              "/api/movies/**","/api/showtimes/**",
                                                "/api/confectionery-categories/**",
                                 "/api/confectionery-products/**","/api/orders/**").permitAll()
                        // Public endpoints for payment and confectionery purchase (including PayPal integration)
                        // Added to allow PayPal payment flow and confectionery purchases without authentication
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/confectionery-purchase", "/api/confectionery-purchase/**", "/api/confectionery-order/**").permitAll()
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
     * <h3>CORS Configuration (from application.yml):</h3>
     * <ul>
     *   <li><strong>Allowed Origins:</strong> Configured via cors.allowed-origins property</li>
     *   <li><strong>Allowed Methods:</strong> Configured via cors.allowed-methods property</li>
     *   <li><strong>Allowed Headers:</strong> Configured via cors.allowed-headers property</li>
     *   <li><strong>Credentials:</strong> Configured via cors.allow-credentials property</li>
     *   <li><strong>Preflight Cache:</strong> Configured via cors.max-age property</li>
     * </ul>
     *
     * <h3>Supported Clients:</h3>
     * <ul>
     *   <li><strong>React Web App:</strong> http://localhost:3000 (development)</li>
     *   <li><strong>Expo Mobile App:</strong> exp://your-ip:8081 (development)</li>
     *   <li><strong>Mobile Emulator:</strong> http://localhost:19006</li>
     *   <li><strong>Production:</strong> https://yourdomain.com</li>
     * </ul>
     *
     * <h3>Configuration Examples:</h3>
     * <pre>
     * # Development (multiple origins)
     * cors.allowed-origins=http://localhost:3000,exp://192.168.1.100:8081
     *
     * # Production
     * cors.allowed-origins=https://yourdomain.com,https://www.yourdomain.com
     * </pre>
     *
     * <p><strong>Security Note:</strong> In production, allowed origins should be
     * restricted to only trusted domains to prevent unauthorized cross-origin requests.
     * Use environment-specific configuration files (application-{profile}.yml) to manage
     * different CORS settings per environment.</p>
     *
     * @return the {@link CorsConfigurationSource} with the defined CORS settings
     *
     * @see CorsConfiguration for detailed CORS configuration options
     * @see UrlBasedCorsConfigurationSource for URL-based CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Parse allowed origins from comma-separated string
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);

        // Parse allowed methods from comma-separated string
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods);

        // Parse allowed headers from comma-separated string
        List<String> headers = Arrays.asList(allowedHeaders.split(","));
        configuration.setAllowedHeaders(headers);

        // Configure credentials support for JWT tokens
        configuration.setAllowCredentials(allowCredentials);

        // Configure preflight request cache duration
        configuration.setMaxAge(maxAge);

        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
