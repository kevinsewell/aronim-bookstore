package com.aronim.bookstore.catalog.domain.event;

import com.aronim.bookstore.catalog.domain.model.BookId;
import com.aronim.bookstore.platform.domain.event.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class BookStockUpdatedEvent extends DomainEvent {
    BookId bookId;
    int quantity;
    int newStock;

    public BookStockUpdatedEvent(BookId bookId, int quantity, int newStock) {
        super();
        this.bookId = bookId;
        this.quantity = quantity;
        this.newStock = newStock;
    }

}
