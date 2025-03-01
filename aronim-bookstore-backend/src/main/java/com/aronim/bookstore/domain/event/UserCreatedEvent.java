package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.UserId;
import lombok.Getter;

/**
 * Event that is published when a new user is created in the system.
 * <p>
 * This domain event carries the identifier of the newly created user,
 * allowing other components to react to user creation.
 * </p>
 */
@Getter
public class UserCreatedEvent extends DomainEvent {
    /**
     * The unique identifier of the newly created user.
     */
    private final UserId userId;

    /**
     * Constructs a new UserCreatedEvent with the given user identifier.
     *
     * @param userId the unique identifier of the newly created user
     */
    public UserCreatedEvent(UserId userId) {
        super();
        this.userId = userId;
    }
}
