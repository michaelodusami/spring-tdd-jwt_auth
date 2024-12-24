package com.github.michaelodusami.fakeazon.modules.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

/**
 * The UserRepository interface provides data access methods for managing User
 * entities.
 * 
 * Purpose:
 * This repository serves as a bridge between the database and the application
 * logic,
 * enabling seamless CRUD (Create, Read, Update, Delete) operations on User
 * entities.
 * 
 * Why It Matters:
 * By extending the Spring Data JPA repository, this interface leverages
 * powerful query
 * generation and customization capabilities, reducing boilerplate and ensuring
 * efficient
 * data management.
 * 
 * Impact on the Application:
 * - Supports user authentication by providing methods like `findByEmail`.
 * - Facilitates role-based user management through custom queries like
 * `findUsersByRole`.
 * - Centralizes user-related database queries, promoting consistency and
 * maintainability.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * 
     * Purpose:
     * This method is critical for user authentication workflows, as the email is
     * used as
     * a unique identifier during login.
     * 
     * Impact:
     * - Provides a quick lookup of users by their email address.
     * - Supports validation of user credentials during authentication.
     * 
     * @param email the email address of the user.
     * @return an Optional containing the user if found, otherwise empty.
     */
    public Optional<User> findByEmail(String email);

    /**
     * Finds all users assigned to a specific role.
     * 
     * Purpose:
     * Enables filtering and retrieval of users based on their roles, which is
     * critical
     * for features like admin management and role-specific dashboards.
     * 
     * Impact:
     * - Supports role-based functionality, such as fetching all "ADMIN" users.
     * - Simplifies role-based access control (RBAC) logic.
     * 
     * @param role the name of the role to filter users by.
     * @return a list of users with the specified role.
     */
    @Query("SELECT u from User u JOIN u.roles r WHERE r = :role")
    List<User> findUsersByRole(@Param("role") String role);
}
