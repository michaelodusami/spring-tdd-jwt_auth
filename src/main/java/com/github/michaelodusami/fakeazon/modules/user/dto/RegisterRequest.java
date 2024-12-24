package com.github.michaelodusami.fakeazon.modules.user.dto;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The RegisterRequest class represents the data required to register a new
 * user.
 *
 * Purpose:
 * This class serves as a Data Transfer Object (DTO) for user registration
 * requests. It encapsulates
 * the user's name, email, and password, ensuring validation rules are enforced
 * before processing the request.
 *
 * Why It Matters:
 * - Simplifies the registration process by standardizing the input format.
 * - Ensures that input validation is handled at the DTO level, improving
 * security and reliability.
 *
 * Impact on the Application:
 * - Prevents invalid or malicious registration requests by enforcing
 * constraints on input fields.
 * - Supports seamless conversion between database entities and API request
 * models.
 *
 * Annotations:
 * - @NoArgsConstructor: Generates a no-argument constructor for flexible object
 * creation.
 * - @AllArgsConstructor: Generates a constructor with all fields as arguments.
 * - @Setter, @Getter: Automatically generates setters and getters for all
 * fields.
 * - @NotBlank: Ensures the field is not null or empty.
 * - @Email: Validates that the field contains a valid email address format.
 * - @Size: Restricts the length of the field.
 *
 * Author: Michael-Andre Odusami
 * Version: 1.0.0
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterRequest {

    /**
     * The full name of the user.
     *
     * Validation:
     * - Must not be blank.
     * - Must have a length between 3 and 50 characters.
     */
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    /**
     * The email address of the user.
     *
     * Validation:
     * - Must not be blank.
     * - Must follow a valid email format.
     */
    @NotBlank
    @Email
    private String email;

    /**
     * The password for the user's account.
     *
     * Validation:
     * - Must not be blank.
     * - Must have a minimum length of 3 characters.
     */
    @NotBlank
    @Size(min = 3)
    private String password;

    /**
     * Converts a User entity into a RegisterRequest object.
     *
     * Purpose:
     * Allows seamless creation of a RegisterRequest object from a User entity,
     * typically for testing
     * or internal operations where such conversion is needed.
     *
     * Impact:
     * - Supports backward conversion of User objects into the format expected for
     * registration requests.
     *
     * @param user the User entity to convert.
     * @return a RegisterRequest object containing the user's name, email, and
     *         password.
     */
    public static RegisterRequest toRegisterRequest(User user) {
        return new RegisterRequest(user.getName(), user.getEmail(), user.getPassword());
    }
}
