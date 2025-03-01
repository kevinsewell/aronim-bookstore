package com.aronim.bookstore.domain.repository;

import com.aronim.bookstore.domain.model.Role;
import com.aronim.bookstore.domain.model.RoleId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Role} entities.
 * Provides methods to perform CRUD operations and queries on Role objects.
 */
public interface RoleRepository {
    /**
     * Saves a role entity to the repository.
     *
     * @param role the role to save
     * @return the saved role entity
     */
    Role save(Role role);

    /**
     * Finds a role by its unique identifier.
     *
     * @param id the role identifier
     * @return an Optional containing the found role or empty if not found
     */
    Optional<Role> findById(RoleId id);

    /**
     * Finds a role by its name.
     *
     * @param name the role name to search for
     * @return an Optional containing the found role or empty if not found
     */
    Optional<Role> findByName(String name);

    /**
     * Retrieves all roles from the repository.
     *
     * @return a list of all roles
     */
    List<Role> findAll();

    /**
     * Deletes a role from the repository.
     *
     * @param role the role to delete
     */
    void delete(Role role);

    /**
     * Checks if a role with the given name exists.
     *
     * @param name the role name to check
     * @return true if a role with the given name exists, false otherwise
     */
    boolean existsByName(String name);
}
