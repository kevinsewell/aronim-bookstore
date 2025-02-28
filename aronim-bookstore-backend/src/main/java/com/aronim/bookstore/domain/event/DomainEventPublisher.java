// File: aronim-bookstore-backend/src/main/java/com/aronim/bookstore/domain/event/DomainEventPublisher.java
package com.aronim.bookstore.domain.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
