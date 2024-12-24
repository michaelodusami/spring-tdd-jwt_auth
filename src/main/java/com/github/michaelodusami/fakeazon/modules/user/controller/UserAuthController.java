package com.github.michaelodusami.fakeazon.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.michaelodusami.fakeazon.modules.user.dto.AuthResponse;
import com.github.michaelodusami.fakeazon.modules.user.dto.LoginRequest;
import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;
import com.github.michaelodusami.fakeazon.security.JwtService;
import com.github.michaelodusami.fakeazon.security.UserDetails;

import jakarta.validation.Valid;

/**
 * The UserAuthController class handles authentication and registration
 * operations for the Fakeazon application.
 *
 * Purpose:
 * - Provides endpoints for user login and registration.
 * - Generates JWT tokens for authenticated users.
 * - Differentiates between standard user and admin registration.
 *
 * Why It Matters:
 * Authentication is a critical part of any application. This controller:
 * - Ensures secure login by validating user credentials and issuing tokens.
 * - Manages user registration and enforces role-based distinctions (e.g., user
 * vs admin).
 *
 * Impact on the Application:
 * - Facilitates secure user access to the application through JWT-based
 * authentication.
 * - Handles user creation with distinct roles for access control.
 * - Centralizes authentication logic to simplify security management.
 *
 * Annotations:
 * - @RestController: Indicates that this class handles RESTful API requests.
 * - @RequestMapping: Defines the base URI for all endpoints in this controller.
 *
 * Author: Michael-Andre Odusami
 * Version: 1.0.0
 */
@RestController
@RequestMapping("/v1/auth")
public class UserAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Handles user login requests.
     *
     * Purpose:
     * Authenticates the user using their email and password, and issues a JWT token
     * upon successful login.
     *
     * Impact:
     * - Verifies the user's credentials using the authentication manager.
     * - Generates a JWT token for secure, stateless session management.
     * - Returns user details and the token to the client for subsequent
     * authenticated requests.
     *
     * @param authRequest the login request containing the user's email and
     *                    password.
     * @return a response entity containing the JWT token and user details, or an
     *         appropriate error status.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest authRequest) {
        try {
            // Authenticate the user using the provided credentials
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            // Retrieve authenticated user's details
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

            // Fetch the user entity from the database
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate JWT token
            String token = jwtService.generateToken(user.getEmail());

            // Return response with the token and user details
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(AuthResponse.toUser(user));
        } catch (BadCredentialsException exception) {
            // Handle invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException exception) {
            // Handle user not found case (optional)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception exception) {
            // Log unexpected errors and return 500 status
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Handles user registration requests.
     *
     * Purpose:
     * Creates a new standard user account with the provided details.
     *
     * Impact:
     * - Saves a new user to the database with the "ROLE_USER" role.
     * - Ensures email uniqueness to prevent duplicate registrations.
     *
     * @param registerRequest the registration request containing user details.
     * @return a response entity with status CREATED if successful, or BAD_REQUEST
     *         for invalid input.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            userService.save(registerRequest, UserRole.ROLE_USER).get();
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (IllegalArgumentException e) {
            // Handle email already registered or other validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Handles admin registration requests.
     *
     * Purpose:
     * Creates a new admin account with the provided details.
     *
     * Impact:
     * - Saves a new user to the database with the "ROLE_ADMIN" role.
     * - Enforces email uniqueness and role distinction for admin users.
     *
     * @param registerRequest the registration request containing user details.
     * @return a response entity with status CREATED if successful, or BAD_REQUEST
     *         for invalid input.
     */
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            userService.save(registerRequest, UserRole.ROLE_ADMIN).get();
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (IllegalArgumentException e) {
            // Handle email already registered or other validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
