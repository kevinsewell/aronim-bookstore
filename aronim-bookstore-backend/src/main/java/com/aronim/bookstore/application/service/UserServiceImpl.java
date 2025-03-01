package com.aronim.bookstore.application.service;

import com.aronim.bookstore.application.dto.RoleDTO;
import com.aronim.bookstore.application.dto.UserDTO;
import com.aronim.bookstore.domain.event.DomainEventPublisher;
import com.aronim.bookstore.domain.model.*;
import com.aronim.bookstore.domain.repository.RoleRepository;
import com.aronim.bookstore.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class that handles user-related operations in the bookstore application.
 * This service manages user creation, retrieval, and password management.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DomainEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the necessary dependencies.
     *
     * @param userRepository  The repository for user data access
     * @param roleRepository  The repository for role data access
     * @param eventPublisher  The publisher for domain events
     * @param passwordEncoder The encoder for password hashing
     */
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           DomainEventPublisher eventPublisher,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

        // Assign default role if it exists
        roleRepository.findByName("ROLE_USER").ifPresent(user::assignRole);

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

    @Transactional
    @Override
    public UserDTO assignRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role role = roleRepository.findById(new RoleId(roleId))
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.assignRole(role);
        userRepository.save(user);

        // Publish events
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        return mapToDTO(user);
    }

    @Transactional
    @Override
    public UserDTO removeRoleFromUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role role = roleRepository.findById(new RoleId(roleId))
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.removeRole(role);
        userRepository.save(user);

        // Publish events
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();

        return mapToDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<RoleDTO> getUserRoles(UUID userId) {
        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getRoles().stream()
                .map(this::mapToRoleDTO)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public RoleDTO createRole(String name, String description) {
        if (roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        Role role = Role.create(
                RoleId.generate(),
                name,
                description
        );

        roleRepository.save(role);

        return mapToRoleDTO(role);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToRoleDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Maps a User domain entity to a UserDTO.
     *
     * @param user The user entity to map
     * @return A DTO representation of the user
     */
    private UserDTO mapToDTO(User user) {
        Set<RoleDTO> roleDTOs = user.getRoles().stream()
                .map(this::mapToRoleDTO)
                .collect(Collectors.toSet());

        return new UserDTO(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getLastLoginAt(),
                user.getStatus().name(),
                roleDTOs
        );
    }

    /**
     * Maps a Role domain entity to a RoleDTO.
     *
     * @param role The role entity to map
     * @return A DTO representation of the role
     */
    private RoleDTO mapToRoleDTO(Role role) {
        return new RoleDTO(
                role.getId().getValue(),
                role.getName(),
                role.getDescription()
        );
    }
}
