package com.aronim.bookstore.catalog.domain.event;

import com.aronim.bookstore.catalog.domain.model.BookId;
import com.aronim.bookstore.platform.domain.event.DomainEvent;
import lombok.Getter;

/**
 * Domain event representing the creation of a new book.
 * <p>
 * This event is triggered when a new book is successfully created in the system.
 * It contains the unique identifier of the created book.
 * </p>
 */
@Getter
public class BookCreatedEvent extends DomainEvent {
    /**
     * The unique identifier of the created book.
     */
    private final BookId bookId;

    /**
     * Constructs a new BookCreatedEvent with the specified book identifier.
     *
     * @param bookId The unique identifier of the created book.
     */
    public BookCreatedEvent(BookId bookId) {
        super();
        this.bookId = bookId;
    }

}
