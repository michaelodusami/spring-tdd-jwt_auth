package com.github.michaelodusami.fakeazon.modules.user.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The User class represents a user account in the Fakeazon eCommerce
 * application.
 * 
 * Purpose:
 * This class serves as a data model for user-related information in the
 * application.
 * It encapsulates critical user details such as name, email, password, roles,
 * and timestamps
 * for account creation and updates.
 * 
 * Why It Matters:
 * Users are a core component of the Fakeazon platform. They enable essential
 * functionalities
 * such as account management, authentication, and authorization. This class
 * ensures
 * consistency and ease of management for user-related data.
 * 
 * Impact on the Application:
 * - Supports user registration and login features.
 * - Enables role-based access control through the roles field.
 * - Provides timestamps to track account creation and modification, which can
 * be used for
 * auditing or analytics purposes.
 * 
 * Annotations:
 * - @Entity: Maps this class to a database table named "users".
 * - @Table: Specifies the table name explicitly.
 * - @NoArgsConstructor, @AllArgsConstructor, @Data, @Builder: Lombok
 * annotations
 * that reduce boilerplate by generating constructors, getters, setters, and
 * builder methods.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User {

    /**
     * The unique identifier for the user.
     * Impact: Provides a primary key for the "users" table, ensuring uniqueness for
     * each user record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the user.
     * Impact: Used for displaying the user's identity in profiles, account
     * settings, and other sections.
     */
    private String name;

    /**
     * The email address of the user.
     * Impact: A primary field for login and communication with the user.
     * Validation: Ensures the email format is valid through the @Email annotation.
     */
    @Email
    private String email;
    private String password;

    /**
     * A collection of roles assigned to the user.
     * Impact: Facilitates role-based access control (e.g., distinguishing between
     * admin and customer).
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    /**
     * The timestamp when the user account was created.
     * Impact: Useful for tracking user registration and account age.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the user account was last updated.
     * Impact: Useful for tracking when the user details were modified.
     */
    private LocalDateTime updatedAt;

    @PostConstruct
    private void initRoles() {
        roles = new HashSet<>();
    }
}
