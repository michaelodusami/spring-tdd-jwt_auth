package com.github.michaelodusami.fakeazon.modules.user.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

import lombok.NonNull;

/**
 * The UserService class manages the business logic related to user operations
 * in the Fakeazon application.
 * 
 * Purpose:
 * This class provides a service layer that interacts with the `UserRepository`
 * for managing user data.
 * It handles operations such as user registration, fetching, updating, and
 * deleting user accounts,
 * ensuring data consistency and enforcing application rules.
 * 
 * Why It Matters:
 * The service layer is crucial for maintaining a clean separation of concerns,
 * allowing controllers
 * to remain lightweight and focused on handling HTTP requests.
 * 
 * Impact on the Application:
 * - Ensures secure password handling through encoding.
 * - Implements business rules like preventing duplicate email registrations.
 * - Facilitates role-based user management and updates.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs the UserService with required dependencies.
     * 
     * @param userRepository  the repository for interacting with the user database.
     * @param passwordEncoder the encoder used to securely hash passwords.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users from the database.
     * 
     * Purpose:
     * Allows administrators or authorized roles to fetch a list of all users.
     * 
     * Impact:
     * Facilitates user management by providing visibility into all registered
     * accounts.
     * 
     * @return a list of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their unique ID.
     * 
     * Purpose:
     * Provides a way to fetch user details using their primary key.
     * 
     * Impact:
     * Supports user-specific operations such as profile management or account
     * updates.
     * 
     * @param id the ID of the user to find.
     * @return an Optional containing the user if found, otherwise empty.
     */
    public Optional<User> findById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their email address.
     * 
     * Purpose:
     * Enables authentication workflows by locating users based on their email.
     * 
     * Impact:
     * Validates if a user exists during login or registration processes.
     * 
     * @param email the email address of the user.
     * @return an Optional containing the user if found, otherwise empty.
     */
    public Optional<User> findByEmail(@NonNull String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registers a new user based on the provided registration request.
     * 
     * Purpose:
     * Creates a new user account while enforcing unique email constraints and
     * encoding passwords.
     * 
     * Impact:
     * Prevents duplicate registrations and ensures secure password storage.
     * 
     * @param registerRequest the details of the user to register.
     * @return an Optional containing the newly registered user.
     */
    public Optional<User> save(@NonNull RegisterRequest registerRequest) {

        if (findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.getRoles().add(UserRole.ROLE_USER.getRole());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }

    public Optional<User> save(@NonNull RegisterRequest registerRequest, UserRole role) {

        if (findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.getRoles().add(role.getRole());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }

    /**
     * Saves a new user with a specific role.
     * 
     * Purpose:
     * Facilitates user creation with predefined roles (e.g., admin or customer).
     * 
     * Impact:
     * Supports role-based account creation workflows.
     * 
     * @param user the user to be saved.
     * @param role the role to assign to the user.
     * @return an Optional containing the saved user.
     */
    public Optional<User> save(@NonNull User user, UserRole role) {

        if (findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.getRoles().add(role.getRole());
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }

    /**
     * Deletes a user by their ID.
     * 
     * Purpose:
     * Removes a user account from the system.
     * 
     * Impact:
     * Enables account deletion for users or administrators.
     * 
     * @param id the ID of the user to delete.
     * @return true if the user was successfully deleted, otherwise false.
     */
    public boolean deleteUser(@NonNull Long id) {
        if (findById(id).isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    /**
     * Updates an existing user's details.
     * 
     * Purpose:
     * Modifies a user's information such as name, email, password, or roles.
     * 
     * Impact:
     * Provides flexibility to keep user information up-to-date.
     * 
     * @param id          the ID of the user to update.
     * @param updatedUser the user object containing updated details.
     * @return an Optional containing the updated user if the update was successful.
     */
    public Optional<User> updateUser(Long id, User updatedUser) {
        // Find the existing user by ID
        return userRepository.findById(id).map(existingUser -> {
            // Update fields from updatedUser
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
                existingUser.setPassword(encodedPassword);
            }
            if (updatedUser.getRoles() != null) {
                existingUser.getRoles().addAll(updatedUser.getRoles());
            }
            // Save and return the updated user
            return userRepository.save(existingUser);
        });
    }

    /**
     * Changes a user's password.
     * 
     * Purpose:
     * Provides a secure way to update a user's password.
     * 
     * Impact:
     * Improves account security by enabling password changes.
     * 
     * @param id          the ID of the user.
     * @param newPassword the new password to set.
     * @return true if the password was successfully updated, otherwise false.
     */
    public boolean changePassword(Long id, String newPassword) {
        return userRepository.findById(id).map(user -> {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    /**
     * Finds users by their role.
     * 
     * Purpose:
     * Retrieves all users assigned to a specific role.
     * 
     * Impact:
     * Facilitates role-based management of users.
     * 
     * @param role the role to filter users by.
     * @return a list of users with the specified role.
     */
    public List<User> findUsersByRole(String role) {
        return userRepository.findUsersByRole(role);
    }

}
