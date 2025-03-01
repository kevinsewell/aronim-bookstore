package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.UserId;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event that represents a change in a user's role.
 * This event is triggered when a user's role is modified in the system.
 * It contains the identifier of the user whose role has been changed.
 */
@Getter
public class UserRoleChangedEvent extends DomainEvent {

    /**
     * The identifier of the user whose role has been changed.
     */
    UserId userId;

    /**
     * Constructs a new UserRoleChangedEvent.
     *
     * @param userId The identifier of the user whose role has been changed
     */
    public UserRoleChangedEvent(UserId userId) {
        super();
        this.userId = userId;
    }
}
