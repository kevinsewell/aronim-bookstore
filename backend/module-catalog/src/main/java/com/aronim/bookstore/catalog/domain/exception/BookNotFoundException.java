package com.aronim.bookstore.catalog.domain.exception;

import com.aronim.bookstore.catalog.domain.model.BookId;
import com.aronim.bookstore.platform.domain.exception.EntityNotFoundException;

/**
 * Exception thrown when a book with a specific ID cannot be found in the catalog.
 * This exception is used in the application layer to indicate that a requested book does not exist.
 */
public class BookNotFoundException extends EntityNotFoundException {
    /**
     * Constructs a new BookNotFoundException with a descriptive error message.
     *
     * @param bookId The identifier of the book that was not found
     */
    public BookNotFoundException(final BookId bookId) {
        super(String.format("Book with ID [%s] not found", bookId));
    }
}
