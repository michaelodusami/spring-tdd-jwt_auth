package com.github.michaelodusami.fakeazon.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;

import java.util.List;

/**
 * The UserController class provides RESTful endpoints for managing users in the
 * Fakeazon application.
 *
 * Purpose:
 * This controller enables administrators and authorized users to perform CRUD
 * operations on user accounts,
 * including retrieving, updating, and deleting users, as well as changing user
 * passwords.
 *
 * Why It Matters:
 * User management is a key part of any application. This controller:
 * - Provides administrators with tools to manage the user base.
 * - Ensures secure operations like password changes.
 * - Centralizes user-related operations for maintainability.
 *
 * Impact on the Application:
 * - Enables authorized personnel to manage users effectively.
 * - Supports secure and structured interaction with the user service layer.
 *
 * Annotations:
 * - @RestController: Indicates that this class is a REST controller.
 * - @RequestMapping: Specifies the base URI for user-related operations.
 * - @Autowired: Injects dependencies automatically.
 *
 * Author: Michael-Andre Odusami
 * Version: 1.0.0
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves a list of all users.
     *
     * Purpose:
     * Provides a way for administrators to view all registered users.
     *
     * Impact:
     * - Facilitates user management and auditing.
     * - Returns user details for administrative purposes.
     *
     * @return a ResponseEntity containing the list of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * Purpose:
     * Allows administrators or authorized users to fetch details of a specific
     * user.
     *
     * Impact:
     * - Provides quick access to user details for management or debugging.
     * - Returns 404 if the user does not exist.
     *
     * @param id the ID of the user to retrieve.
     * @return a ResponseEntity containing the user details or a 404 status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by their email address.
     *
     * Purpose:
     * Enables retrieval of user details based on their email.
     *
     * Impact:
     * - Supports user management tasks requiring email-based identification.
     * - Returns 404 if the user is not found.
     *
     * @param email the email of the user to retrieve.
     * @return a ResponseEntity containing the user details or a 404 status.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing user's details.
     *
     * Purpose:
     * Provides administrators with a way to modify user information.
     *
     * Impact:
     * - Enables user profile corrections or updates.
     * - Returns 404 if the user to be updated does not exist.
     *
     * @param id          the ID of the user to update.
     * @param updatedUser the user object containing updated details.
     * @return a ResponseEntity containing the updated user or a 404 status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a user by their ID.
     *
     * Purpose:
     * Allows administrators to remove user accounts from the system.
     *
     * Impact:
     * - Supports account deletion for inactive or invalid users.
     * - Returns 404 if the user to be deleted does not exist.
     *
     * @param id the ID of the user to delete.
     * @return a ResponseEntity with a success status or a 404 status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Changes the password of an existing user.
     *
     * Purpose:
     * Provides a way for administrators or users to update passwords securely.
     *
     * Impact:
     * - Enhances security by allowing password updates.
     * - Returns 404 if the user to be updated does not exist.
     *
     * @param id          the ID of the user whose password is to be changed.
     * @param newPassword the new password for the user.
     * @return a ResponseEntity indicating success or failure.
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        boolean updated = userService.changePassword(id, newPassword);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
