package com.cinerama.backend.security;

import com.cinerama.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Validates JWT tokens from Authorization headers and establishes Spring Security authentication context.
 *
 * <p>This filter enables stateless authentication by intercepting requests and validating Bearer tokens.
 * Successfully authenticated users have their identity stored in the SecurityContext for the request lifecycle.</p>
 *
 * <p>Expected token format: {@code Authorization: Bearer {jwt_token}}</p>
 *
 * <p>Requests without valid tokens continue as anonymous users, allowing access to public endpoints
 * while protecting secured resources through Spring Security's authorization mechanisms.</p>
 *
 * @author Cinerama Development Team
 * @see JwtUtil for token validation utilities
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Processes incoming requests to authenticate users via JWT tokens.
     *
     * <p>Invalid or missing tokens result in anonymous access, while valid tokens
     * establish authenticated user context for downstream security checks.</p>
     *
     * @param request the HTTP request containing potential JWT token
     * @param response the HTTP response (unused in this filter)
     * @param filterChain the security filter chain to continue processing
     * @throws ServletException if servlet processing fails
     * @throws IOException if request/response processing fails
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Skip authentication if no Bearer token present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        // Only set authentication context for valid tokens
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            // Create authenticated user context without roles (handled elsewhere)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
