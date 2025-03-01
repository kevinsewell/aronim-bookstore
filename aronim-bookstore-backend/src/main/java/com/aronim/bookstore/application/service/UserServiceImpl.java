package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.UserDTO;
import com.aronim.bookstore.domain.event.DomainEventPublisher;
import com.aronim.bookstore.domain.model.Email;
import com.aronim.bookstore.domain.model.Password;
import com.aronim.bookstore.domain.model.User;
import com.aronim.bookstore.domain.model.UserId;
import com.aronim.bookstore.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class that handles user-related operations in the bookstore application.
 * This service manages user creation, retrieval, and password management.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the necessary dependencies.
     *
     * @param userRepository  The repository for user data access
     * @param eventPublisher  The publisher for domain events
     * @param passwordEncoder The encoder for password hashing
     */
    public UserServiceImpl(UserRepository userRepository,
                           DomainEventPublisher eventPublisher,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDTO createUser(String email, String password, String firstName, String lastName) {
        Email emailVO = new Email(email);
        if (userRepository.existsByEmail(emailVO)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.create(
                emailVO,
                new Password(passwordEncoder.encode(password)),
                firstName,
                lastName
        );

        userRepository.save(user);

        // Publish events
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        return mapToDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDTO> findById(UUID id) {
        return userRepository.findById(new UserId(id))
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(new Email(email))
                .map(this::mapToDTO);
    }

    @Transactional
    @Override
    public void changePassword(UUID id, String newPassword) {
        userRepository.findById(new UserId(id)).ifPresent(user -> {
            user.changePassword(new Password(passwordEncoder.encode(newPassword)));
            userRepository.save(user);

            // Publish events
            user.getDomainEvents().forEach(eventPublisher::publish);
            user.clearDomainEvents();
        });
    }

    /**
     * Maps a User domain entity to a UserDTO.
     *
     * @param user The user entity to map
     * @return A DTO representation of the user
     */
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getLastLoginAt(),
                user.getStatus().name()
        );
    }
}
