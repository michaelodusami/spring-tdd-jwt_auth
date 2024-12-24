package com.github.michaelodusami.fakeazon.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.github.michaelodusami.fakeazon.security.CustomUserDetailsService;
import com.github.michaelodusami.fakeazon.security.JwtAuthFilter;

/**
 * The SecurityConfig class configures Spring Security for the Fakeazon
 * application.
 *
 * Purpose:
 * This configuration establishes security measures to protect the application,
 * including:
 * - Configuring authentication and password encoding with a custom
 * `UserDetailsService`.
 * - Enabling role-based access control through JWT token validation.
 * - Defining public and protected endpoints with authorization rules.
 * - Implementing CORS settings for frontend-backend communication.
 * - Managing sessions in a stateless manner to improve scalability.
 *
 * Why It Matters:
 * Secure user authentication, authorization, and data protection are critical
 * for any application.
 * This configuration:
 * - Protects sensitive user data and resources from unauthorized access.
 * - Enables robust JWT-based stateless authentication for scalability.
 * - Configures CORS to allow controlled communication with external clients.
 *
 * Impact on the Application:
 * - Provides a secure foundation for protecting endpoints and user data.
 * - Integrates CORS to allow cross-origin requests for modern frontend-backend
 * architectures.
 * - Ensures that only authenticated users can access protected endpoints.
 *
 * Annotations:
 * - @Configuration: Marks this class as a Spring configuration class.
 * - @EnableWebSecurity: Enables Spring Security for the application.
 *
 * Author: Michael-Andre Odusami
 * Version: 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Constructs the SecurityConfig with required dependencies.
     *
     * @param userDetailsService the custom service to load user details during
     *                           authentication.
     * @param jwtAuthFilter      the filter for validating and processing JWTs.
     */
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configures the security filter chain for the application.
     *
     * Purpose:
     * Defines the behavior of Spring Security for handling authentication,
     * authorization,
     * session management, and JWT validation.
     *
     * Impact:
     * - Public endpoints for user registration and login are accessible without
     * authentication.
     * - All other endpoints are protected and require valid JWT authentication.
     * - Stateless session management ensures scalability and removes server-side
     * session storage.
     *
     * @param http the `HttpSecurity` object used to configure security settings.
     * @return a configured `SecurityFilterChain` object.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disables CSRF protection for stateless JWT-based authentication.
                .cors(Customizer.withDefaults()) // Enables CORS with the defined CORS configuration.
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1/auth/register", "/v1/auth/login", "/v1/auth/register/admin").permitAll()
                        .anyRequest().authenticated() // Protects all other endpoints.
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Enforces
                                                                                                             // stateless
                                                                                                             // session
                                                                                                             // management.
                )
                .authenticationProvider(authenticationProvider()) // Configures custom authentication provider.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adds JWT filter before
                                                                                             // default filters.

        return http.build();
    }

    /**
     * Configures a `PasswordEncoder` bean for hashing passwords.
     *
     * Purpose:
     * Provides a strong hashing mechanism for securely storing user passwords in
     * the database.
     *
     * Impact:
     * - Protects user passwords from brute-force and rainbow table attacks.
     * - Ensures compatibility with Spring Security's default password encoding
     * requirements.
     *
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an `AuthenticationManager` bean.
     *
     * Purpose:
     * Exposes the authentication manager to handle user authentication requests.
     *
     * Impact:
     * Allows programmatic authentication of users through services or controllers.
     *
     * @param config the `AuthenticationConfiguration` provided by Spring Security.
     * @return a configured `AuthenticationManager` instance.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures a `DaoAuthenticationProvider` bean.
     *
     * Purpose:
     * Sets up the authentication provider to use the custom `UserDetailsService`
     * and password encoder.
     *
     * Impact:
     * - Delegates authentication to the custom service for user validation.
     * - Ensures secure comparison of passwords using the password encoder.
     *
     * @return a configured `DaoAuthenticationProvider` instance.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures a CORS policy for cross-origin requests.
     *
     * Purpose:
     * Allows communication between the frontend and backend by enabling
     * cross-origin requests
     * from specified origins, methods, and headers.
     *
     * Impact:
     * - Prevents CORS errors when the application is accessed from different
     * origins.
     * - Provides a controlled mechanism for secure cross-origin interactions.
     *
     * @return a `CorsConfigurationSource` with the defined CORS policy.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Allows requests from all origins (adjust for
                                                             // production).
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP
                                                                                                   // methods.
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token")); // Allowed
                                                                                                         // headers.
        configuration.setExposedHeaders(Arrays.asList("x-auth-token", "Authorization")); // Headers exposed to clients.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applies the configuration to all endpoints.
        return source;
    }
}
