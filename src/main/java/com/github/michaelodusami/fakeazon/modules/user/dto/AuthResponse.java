package com.github.michaelodusami.fakeazon.modules.user.dto;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The AuthResponse class represents the structure of the response sent back to
 * the client after successful authentication.
 *
 * Purpose:
 * This class serves as a Data Transfer Object (DTO) to encapsulate and return
 * essential user information
 * in response to authentication requests. It ensures that sensitive
 * information, such as passwords, is not exposed.
 *
 * Why It Matters:
 * - Provides a clear and secure way to share user details with clients
 * post-login.
 * - Helps maintain separation of concerns by decoupling the API response
 * structure from the database entity.
 *
 * Impact on the Application:
 * - Improves security by excluding sensitive data from API responses.
 * - Simplifies the client-side handling of user-related information by
 * returning only relevant fields.
 *
 * Annotations:
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @AllArgsConstructor: Generates a constructor with all fields as arguments.
 * - @Setter, @Getter: Generate setters and getters for all fields.
 * - @ToString: Generates a `toString()` method for easy debugging and logging.
 *
 * Author: Michael-Andre Odusami
 * Version: 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AuthResponse {
    private Long id; // The unique identifier of the user.
    private String name; // The name of the user.
    private String email; // The email address of the user.

    /**
     * Converts a User entity to an AuthResponse DTO.
     *
     * Purpose:
     * Extracts relevant fields from the User entity and maps them to an
     * AuthResponse object
     * to be sent as part of the authentication response.
     *
     * Impact:
     * - Prevents sensitive fields (like passwords) from being included in API
     * responses.
     * - Standardizes the response format for authentication endpoints.
     *
     * @param user the User entity to convert.
     * @return an AuthResponse containing the user's ID, name, and email.
     */
    public static AuthResponse toUser(User user) {
        return new AuthResponse(user.getId(), user.getName(), user.getEmail());
    }
}
