package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.BookId;
import lombok.Getter;

@Getter
public class BookCreatedEvent extends DomainEvent {
    private final BookId bookId;

    public BookCreatedEvent(BookId bookId) {
        super();
        this.bookId = bookId;
    }

}
