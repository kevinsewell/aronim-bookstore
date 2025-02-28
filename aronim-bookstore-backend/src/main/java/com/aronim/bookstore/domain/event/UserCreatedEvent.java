package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.UserId;
import lombok.Getter;

@Getter
public class UserCreatedEvent extends DomainEvent {
    private final UserId userId;

    public UserCreatedEvent(UserId userId) {
        super();
        this.userId = userId;
    }
}
