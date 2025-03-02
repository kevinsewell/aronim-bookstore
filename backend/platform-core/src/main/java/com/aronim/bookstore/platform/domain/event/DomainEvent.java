package com.aronim.bookstore.platform.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
public abstract class DomainEvent {
    Instant occurredOn;

    protected DomainEvent() {
        this.occurredOn = Instant.now();
    }

}
