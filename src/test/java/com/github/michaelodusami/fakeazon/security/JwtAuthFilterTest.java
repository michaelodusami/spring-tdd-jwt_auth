package com.github.michaelodusami.fakeazon.security;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @SpringBootTest
public class JwtAuthFilterTest {
    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSkipFilterForPublicEndpoints() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/v1/auth/register");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateWithValidToken() throws ServletException, IOException {
        // Mock request with a valid token
        String token = "valid.jwt.token";
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(request.getServletPath()).thenReturn("/v1/users");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(jwtService.validateToken(eq(token), any())).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verify SecurityContext is populated
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(email, authentication.getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipAuthenticationForInvalidToken() throws ServletException, IOException {
        // Mock request with an invalid token
        String token = "invalid.jwt.token";

        when(request.getServletPath()).thenReturn("/v1/users");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Invalid Token"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verify SecurityContext is not populated
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipWhenNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/v1/users");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
