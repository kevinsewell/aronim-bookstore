package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.UserDTO;
import com.aronim.bookstore.domain.event.DomainEventPublisher;
import com.aronim.bookstore.domain.model.*;
import com.aronim.bookstore.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                      DomainEventPublisher eventPublisher,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
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
    public Optional<UserDTO> findById(UUID id) {
        return userRepository.findById(new UserId(id))
                           .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(new Email(email))
                           .map(this::mapToDTO);
    }

    @Transactional
    public void changePassword(UUID id, String newPassword) {
        userRepository.findById(new UserId(id)).ifPresent(user -> {
            user.changePassword(new Password(passwordEncoder.encode(newPassword)));
            userRepository.save(user);
            
            // Publish events
            user.getDomainEvents().forEach(eventPublisher::publish);
            user.clearDomainEvents();
        });
    }

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
