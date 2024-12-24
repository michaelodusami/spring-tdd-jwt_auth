package com.github.michaelodusami.fakeazon.modules.user.dto;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The LoginRequest class represents the structure of the data submitted by the
 * client for user authentication.
 *
 * Purpose:
 * This class serves as a Data Transfer Object (DTO) for login requests. It
 * encapsulates the user's email and password,
 * which are required for authentication.
 *
 * Why It Matters:
 * - Provides a clear structure for handling login request payloads.
 * - Simplifies mapping and validation of login credentials submitted by the
 * client.
 *
 * Impact on the Application:
 * - Facilitates secure and standardized authentication by ensuring proper input
 * format.
 * - Supports the creation of reusable methods for handling login-related data
 * transformations.
 *
 * Annotations:
 * - @NoArgsConstructor: Generates a no-argument constructor for flexible object
 * creation.
 * - @AllArgsConstructor: Generates a constructor with all fields as arguments.
 * - @Setter, @Getter: Generate setters and getters for all fields.
 *
 * Author: Michael-Andre Odusami
 * Version: 1.0.0
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginRequest {

    private String email; // The email address of the user attempting to log in.
    private String password; // The user's password for authentication.

    /**
     * Converts a User entity into a LoginRequest object.
     *
     * Purpose:
     * Allows the creation of a LoginRequest object from a User entity, primarily
     * for testing
     * or internal operations that require email-password data.
     *
     * Impact:
     * - Supports seamless conversion of `User` objects into login request format.
     * - Helps in scenarios where user data needs to be mapped into a login request
     * for simulations or internal processes.
     *
     * @param user the User entity to convert.
     * @return a LoginRequest object containing the user's email and password.
     */
    public static LoginRequest toLoginRequest(User user) {
        return new LoginRequest(user.getEmail(), user.getPassword());
    }
}
