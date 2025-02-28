package com.aronim.bookstore.domain.model;

import com.aronim.bookstore.domain.event.BookCreatedEvent;
import com.aronim.bookstore.domain.event.BookStockUpdatedEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Book extends AggregateRoot {
    private BookId id;
    private ISBN isbn;
    private Title title;
    private Author author;
    private Publisher publisher;
    private LocalDate publishDate;
    private BigDecimal price;
    private int stockQuantity;
    private BookStatus status;

    private Book() {
    }

    public static Book create(ISBN isbn, Title title, Author author, Publisher publisher) {
        Book book = new Book();
        book.id = new BookId(UUID.randomUUID());
        book.isbn = isbn;
        book.title = title;
        book.author = author;
        book.publisher = publisher;
        book.status = BookStatus.AVAILABLE;
        book.stockQuantity = 0;

        // Register the creation event
        book.registerEvent(new BookCreatedEvent(book.id));

        return book;
    }

    public void updateStock(int quantity) {
        if (quantity < 0 && Math.abs(quantity) > this.stockQuantity) {
            throw new IllegalStateException("Cannot remove more books than available in stock");
        }
        int oldStock = this.stockQuantity;
        this.stockQuantity += quantity;
        updateStatus();

        // Register the stock update event
        registerEvent(new BookStockUpdatedEvent(this.id, quantity, this.stockQuantity));
    }

    private void updateStatus() {
        if (this.stockQuantity <= 0) {
            this.status = BookStatus.OUT_OF_STOCK;
        } else {
            this.status = BookStatus.AVAILABLE;
        }
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.price = newPrice;
    }

    // Getters
    public BookId getId() {
        return id;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public Title getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public BookStatus getStatus() {
        return status;
    }
}
