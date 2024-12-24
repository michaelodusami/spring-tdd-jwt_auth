package com.github.michaelodusami.fakeazon.modules.user.enums;

/**
 * The UserRole enum defines the roles available in the Fakeazon application.
 * 
 * Purpose:
 * This enum encapsulates user roles, providing a type-safe way to manage and enforce
 * role-based access control (RBAC) across the application.
 * 
 * Why It Matters:
 * Role management is essential for implementing secure access to application features.
 * This enum prevents invalid role assignments and simplifies role handling.
 * 
 * Impact on the Application:
 * - Centralizes role definitions, reducing the risk of inconsistencies.
 * - Facilitates features like admin dashboards and restricted user functionalities.
 * - Enhances code readability and maintainability by replacing string literals with constants.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
public enum UserRole {
    /**
     * Represents a standard user with access to basic application functionalities.
     */
    ROLE_USER("USER"),

    /**
     * Represents an administrator with elevated permissions for managing users and system settings.
     */
    ROLE_ADMIN("ADMIN");

    private final String role;

    /**
     * Constructs a UserRole with the specified role name.
     * 
     * @param role the name of the role.
     */
    UserRole(String role) {
        this.role = role;
    }

    /**
     * Retrieves the string representation of the role.
     * 
     * Purpose:
     * Provides a way to get the role name for storage or comparison.
     * 
     * Impact:
     * Facilitates role-based checks and assignments throughout the application.
     * 
     * @return the string representation of the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Converts a string to its corresponding UserRole.
     * 
     * Purpose:
     * Provides a utility method to convert external input (e.g., API payloads) into
     * a UserRole instance.
     * 
     * Impact:
     * Validates and maps input strings to predefined roles, ensuring correctness
     * and avoiding invalid role assignments.
     * 
     * @param role the string representation of the role.
     * @return the corresponding UserRole.
     * @throws IllegalArgumentException if no matching role is found.
     */
    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("No role found for: " + role);
    } 
}
