package com.aronim.bookstore.domain.repository;

import com.aronim.bookstore.domain.model.Email;
import com.aronim.bookstore.domain.model.User;
import com.aronim.bookstore.domain.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} aggregates in the system.
 */
public interface UserRepository {
    /**
     * Saves a user entity to the repository.
     *
     * @param user the user to save
     */
    void save(User user);

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the user identifier
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findById(UserId id);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(Email email);

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users
     */
    List<User> findAll();

    /**
     * Deletes a user from the repository by their identifier.
     *
     * @param id the identifier of the user to delete
     */
    void delete(UserId id);

    /**
     * Checks if a user with the given email exists in the repository.
     *
     * @param email the email address to check
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(Email email);
}
