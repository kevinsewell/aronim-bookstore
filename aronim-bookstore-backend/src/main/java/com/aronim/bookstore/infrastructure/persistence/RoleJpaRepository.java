package com.aronim.bookstore.infrastructure.persistence;

import com.aronim.bookstore.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository interface for managing {@link RoleEntity} persistence operations.
 * Provides standard CRUD operations through JpaRepository inheritance and additional
 * role-specific query methods.
 */
public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to find
     * @return an Optional containing the role if found, or empty if not found
     */
    Optional<RoleEntity> findByName(String name);

    /**
     * Checks if a role with the specified name exists.
     *
     * @param name the name to check
     * @return true if a role with the given name exists, false otherwise
     */
    boolean existsByName(String name);
}
