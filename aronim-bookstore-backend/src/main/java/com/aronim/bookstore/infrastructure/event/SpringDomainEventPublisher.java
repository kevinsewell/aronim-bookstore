package com.aronim.bookstore.infrastructure.event;

import com.aronim.bookstore.domain.event.DomainEvent;
import com.aronim.bookstore.domain.event.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring implementation of the DomainEventPublisher interface.
 * This class acts as a bridge between domain events and Spring's event publishing mechanism.
 * It uses Spring's ApplicationEventPublisher to broadcast domain events throughout the application.
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {
    /**
     * Spring's event publisher used to broadcast events.
     */
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs a new SpringDomainEventPublisher.
     *
     * @param applicationEventPublisher The Spring event publisher to use for broadcasting events
     */
    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publishes a domain event using Spring's event publishing mechanism.
     *
     * @param event The domain event to publish
     */
    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
