package com.aronim.bookstore.domain.event;

/**
 * Interface for publishing domain events in the system.
 * <p>
 * The DomainEventPublisher is responsible for broadcasting domain events to interested listeners
 * within the application. It serves as an abstraction over the actual event publishing mechanism,
 * allowing for different implementations (e.g., synchronous, asynchronous, or distributed).
 * </p>
 * 
 * <p>
 * This publisher is typically used in conjunction with the {@link DomainEvent} base class and is
 * integrated into the domain model through the {@link com.aronim.bookstore.domain.model.AggregateRoot}
 * class.
 * </p>
 * 
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * public class BookService {
 *     private final DomainEventPublisher eventPublisher;
 *     
 *     public void createBook(Book book) {
 *         // ... business logic ...
 *         book.getDomainEvents().forEach(eventPublisher::publish);
 *         book.clearDomainEvents();
 *     }
 * }
 * }
 * </pre>
 * </p>
 * 
 * @see DomainEvent
 * @see com.aronim.bookstore.domain.model.AggregateRoot
 * @see com.aronim.bookstore.infrastructure.event.SpringDomainEventPublisher
 */
public interface DomainEventPublisher {

    /**
     * Publishes a domain event to all registered listeners.
     * <p>
     * This method is responsible for broadcasting the given domain event to all interested
     * listeners in the system. The actual delivery mechanism and timing may vary depending
     * on the implementation (synchronous, asynchronous, etc.).
     * </p>
     * 
     * @param event the domain event to publish. Must not be null.
     * @throws IllegalArgumentException if the event is null
     * @throws RuntimeException if there is an error during event publication
     */
    void publish(DomainEvent event);
}
