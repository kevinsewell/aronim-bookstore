package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing user operations in the bookstore system.
 * Provides functionality for user creation, retrieval, and password management.
 * All operations are transactional to ensure data consistency.
 */
public interface UserService {
    /**
     * Creates a new user with the provided information.
     *
     * @param email     User's email address
     * @param password  User's password (will be encoded)
     * @param firstName User's first name
     * @param lastName  User's last name
     * @return A DTO containing the created user's information
     * @throws IllegalArgumentException if the email already exists in the system
     */
    @Transactional
    UserDTO createUser(String email, String password, String firstName, String lastName);

    /**
     * Finds a user by their ID.
     *
     * @param id The UUID of the user to find
     * @return An Optional containing the user DTO if found, empty otherwise
     */
    @Transactional(readOnly = true)
    Optional<UserDTO> findById(UUID id);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return An Optional containing the user DTO if found, empty otherwise
     */
    @Transactional(readOnly = true)
    Optional<UserDTO> findByEmail(String email);

    /**
     * Changes a user's password.
     *
     * @param id          The UUID of the user whose password will be changed
     * @param newPassword The new password (will be encoded)
     */
    @Transactional
    void changePassword(UUID id, String newPassword);
}
