package com.aronim.bookstore.domain.model;

import com.aronim.bookstore.domain.event.UserCreatedEvent;
import com.aronim.bookstore.domain.event.UserPasswordChangedEvent;
import com.aronim.bookstore.domain.event.UserRoleChangedEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class User extends AggregateRoot {
    private UserId id;
    private Email email;
    private Password password;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private UserStatus status;
    private Set<Role> roles;

    private User() {
        // Private constructor for factory method
        this.roles = new HashSet<>();
    }

    public static User create(Email email, Password password, String firstName, String lastName) {
        User user = new User();
        user.id = new UserId(UUID.randomUUID());
        user.email = email;
        user.password = password;
        user.firstName = firstName;
        user.lastName = lastName;
        user.createdAt = LocalDateTime.now();
        user.status = UserStatus.ACTIVE;
        user.roles = new HashSet<>();

        user.registerEvent(new UserCreatedEvent(user.id));
        return user;
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
        registerEvent(new UserPasswordChangedEvent(this.id));
    }

    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive");
        }
        this.status = UserStatus.INACTIVE;
    }

    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void assignRole(Role role) {
        this.roles.add(role);
        registerEvent(new UserRoleChangedEvent(this.id));
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        registerEvent(new UserRoleChangedEvent(this.id));
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
