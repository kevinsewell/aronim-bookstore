package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.BookId;

public class BookCreatedEvent extends DomainEvent {
    private final BookId bookId;

    public BookCreatedEvent(BookId bookId) {
        super();
        this.bookId = bookId;
    }

    public BookId getBookId() {
        return bookId;
    }
}
