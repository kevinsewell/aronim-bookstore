package com.aronim.bookstore.infrastructure.persistence;

import com.aronim.bookstore.domain.model.*;
import com.aronim.bookstore.domain.repository.UserRepository;
import com.aronim.bookstore.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(User user) {
        jpaRepository.save(mapToEntity(user));
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
                          .map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                          .map(this::mapToDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                          .map(this::mapToDomain)
                          .collect(Collectors.toList());
    }

    @Override
    public void delete(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    private UserEntity mapToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId().getValue());
        entity.setEmail(user.getEmail().getValue());
        entity.setPasswordHash(user.getPassword().getHashedValue());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setLastLoginAt(user.getLastLoginAt());
        entity.setStatus(user.getStatus().name());
        return entity;
    }

    private User mapToDomain(UserEntity entity) {
        // Note: This is a simplified mapping that creates a new user
        // In a real implementation, you might want to use reflection or a different
        // construction mechanism to recreate the exact state of the user
        User user = User.create(
            new Email(entity.getEmail()),
            new Password(entity.getPasswordHash()),
            entity.getFirstName(),
            entity.getLastName()
        );

        // Reflect the actual state from the database
        // This might require adding package-private setters or a different construction mechanism
        // depending on your domain model's requirements
        if (entity.getLastLoginAt() != null) {
            user.recordLogin();
        }

        if ("INACTIVE".equals(entity.getStatus())) {
            user.deactivate();
        }

        return user;
    }
}
