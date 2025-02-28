package com.aronim.bookstore.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class BookDTO {
    private final UUID id;
    private final String isbn;
    private final String title;
    private final String author;
    private final String publisher;
    private final LocalDate publishDate;
    private final BigDecimal price;
    private final int stockQuantity;
    private final String status;

    public BookDTO(UUID id, String isbn, String title, String author, String publisher,
                   LocalDate publishDate, BigDecimal price, int stockQuantity, String status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getStatus() {
        return status;
    }
}
