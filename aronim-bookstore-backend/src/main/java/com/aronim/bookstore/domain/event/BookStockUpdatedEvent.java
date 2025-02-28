package com.aronim.bookstore.domain.event;

import com.aronim.bookstore.domain.model.BookId;

public class BookStockUpdatedEvent extends DomainEvent {
    private final BookId bookId;
    private final int quantity;
    private final int newStock;

    public BookStockUpdatedEvent(BookId bookId, int quantity, int newStock) {
        super();
        this.bookId = bookId;
        this.quantity = quantity;
        this.newStock = newStock;
    }

    public BookId getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNewStock() {
        return newStock;
    }
}
