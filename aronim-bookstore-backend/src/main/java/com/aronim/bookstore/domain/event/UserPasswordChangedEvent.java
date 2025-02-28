package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.UserId;
import lombok.Getter;

@Getter
public class UserPasswordChangedEvent extends DomainEvent {
    private final UserId userId;

    public UserPasswordChangedEvent(UserId userId) {
        super();
        this.userId = userId;
    }
}
