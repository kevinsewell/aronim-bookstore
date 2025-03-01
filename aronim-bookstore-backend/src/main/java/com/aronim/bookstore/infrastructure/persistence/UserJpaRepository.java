package com.aronim.bookstore.infrastructure.persistence;

import com.aronim.bookstore.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for managing user entities in the database.
 * Provides methods for CRUD operations on {@link UserEntity} objects.
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Checks if a user with the specified email exists.
     *
     * @param email the email address to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
