package com.aronim.bookstore.domain.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class DomainEvent {
    private final Instant occurredOn;

    protected DomainEvent() {
        this.occurredOn = Instant.now();
    }

}
