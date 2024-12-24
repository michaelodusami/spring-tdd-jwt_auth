package com.github.michaelodusami.fakeazon.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The JwtAuthFilter class is a custom Spring Security filter that intercepts
 * requests,
 * validates JWTs, and sets the authentication context if the token is valid.
 * 
 * Purpose:
 * This filter ensures that incoming requests include a valid JWT for
 * authentication.
 * It extracts the token from the `Authorization` header, validates it, and sets
 * the
 * authentication context if the token is valid.
 * 
 * Why It Matters:
 * Filters like this are critical for securing REST APIs by ensuring that only
 * authenticated
 * users can access protected endpoints.
 * 
 * Impact on the Application:
 * - Validates JWTs in real-time to enforce secure access to resources.
 * - Sets the security context for authenticated users, enabling role-based
 * access control.
 * - Rejects requests with invalid or missing tokens without invoking the
 * controller logic.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private UserRepository userRepository;
    private JwtService jwtService;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * Filters incoming requests to validate the JWT in the `Authorization` header.
     * 
     * Purpose:
     * Checks if the request contains a valid JWT. If valid, it sets the
     * authentication
     * context for the user. Otherwise, the request proceeds without authentication.
     * 
     * Impact:
     * - Validates the JWT and extracts user details.
     * - Sets the authentication context for the current request.
     * - Prevents unauthorized access to protected endpoints.
     * 
     * @param request     the incoming HTTP request.
     * @param response    the HTTP response to be sent.
     * @param filterChain the filter chain to pass control to the next filter.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs during request processing.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.split(" ")[1].trim();
            String username = jwtService.extractUsername(token);
            var userOptional = userRepository.findByEmail(username);

            if (userOptional.isPresent()) {

                var user = userOptional.get();
                var userDetails = new UserDetails(user);

                if (jwtService.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities() // Use authorities from UserDetails
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (RuntimeException ex) {

            // Log the exception and skip setting the SecurityContext
            System.err.println("JWT validation failed: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
