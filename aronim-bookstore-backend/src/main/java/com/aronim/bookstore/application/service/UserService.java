package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.RoleDTO;
import com.aronim.bookstore.application.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
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

    /**
     * Assigns a role to a user.
     *
     * @param userId The UUID of the user
     * @param roleId The UUID of the role to assign
     * @return The updated user DTO
     */
    @Transactional
    UserDTO assignRoleToUser(UUID userId, UUID roleId);

    /**
     * Removes a role from a user.
     *
     * @param userId The UUID of the user
     * @param roleId The UUID of the role to remove
     * @return The updated user DTO
     */
    @Transactional
    UserDTO removeRoleFromUser(UUID userId, UUID roleId);

    /**
     * Gets all roles assigned to a user.
     *
     * @param userId The UUID of the user
     * @return A set of role DTOs
     */
    @Transactional(readOnly = true)
    Set<RoleDTO> getUserRoles(UUID userId);

    /**
     * Creates a new role.
     *
     * @param name        The name of the role
     * @param description The description of the role
     * @return A DTO containing the created role's information
     */
    @Transactional
    RoleDTO createRole(String name, String description);

    /**
     * Gets all available roles in the system.
     *
     * @return A set of all role DTOs
     */
    @Transactional(readOnly = true)
    Set<RoleDTO> getAllRoles();
}
