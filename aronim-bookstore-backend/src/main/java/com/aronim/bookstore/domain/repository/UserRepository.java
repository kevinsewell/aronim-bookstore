package com.aronim.bookstore.domain.repository;

import com.aronim.bookstore.domain.model.User;
import com.aronim.bookstore.domain.model.UserId;
import com.aronim.bookstore.domain.model.Email;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} aggregates in the system.
 */
public interface UserRepository {
    void save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    List<User> findAll();

    void delete(UserId id);

    boolean existsByEmail(Email email);
}
