package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.BookId;

import java.time.Instant;

public abstract class DomainEvent {
    private final Instant occurredOn;

    protected DomainEvent() {
        this.occurredOn = Instant.now();
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
