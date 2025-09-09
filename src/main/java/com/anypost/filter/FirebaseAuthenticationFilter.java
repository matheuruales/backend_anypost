package com.anypost.filter;

import com.anypost.service.FirebaseService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationFilter.class);
    private final FirebaseService firebaseService;

    public FirebaseAuthenticationFilter(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip authentication for public endpoints
        if (path.equals("/api/auth/health") || path.equals("/api/auth/verify")) {
            logger.debug("Skipping authentication for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String idToken = authorizationHeader.substring(7);

            try {
                var authentication = firebaseService.authenticate(idToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Successfully authenticated user: {}", authentication.getName());
                }
            } catch (Exception e) {
                logger.error("Failed to authenticate Firebase token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid authentication token\"}");
                return;
            }
        } else {
            logger.warn("No Authorization header found for protected endpoint: {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Authorization header required\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/health") || path.equals("/api/auth/verify");
    }
}