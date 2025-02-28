package com.aronim.bookstore.infrastructure.persistence.entity;

import com.aronim.bookstore.domain.model.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author_first_name", nullable = false)
    private String authorFirstName;

    @Column(name = "author_last_name", nullable = false)
    private String authorLastName;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status;

    // Default constructor for JPA
    protected BookEntity() {}

    public static BookEntity fromDomain(Book book) {
        BookEntity entity = new BookEntity();
        entity.id = book.getId().getValue();
        entity.isbn = book.getIsbn().getValue();
        entity.title = book.getTitle().getValue();
        entity.authorFirstName = book.getAuthor().getFirstName();
        entity.authorLastName = book.getAuthor().getLastName();
        entity.publisher = book.getPublisher().getName();
        entity.publishDate = book.getPublishDate();
        entity.price = book.getPrice();
        entity.stockQuantity = book.getStockQuantity();
        entity.status = book.getStatus();
        return entity;
    }

    public Book toDomain() {
        Book book = Book.create(
            new ISBN(isbn),
            new Title(title),
            new Author(authorFirstName, authorLastName),
            new Publisher(publisher)
        );
        
        // Use reflection or other method to set the ID (which is normally immutable)
        // This is a simplified approach - in a real app you might use a different pattern
        try {
            java.lang.reflect.Field idField = Book.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(book, new BookId(id));
            idField.setAccessible(false);
            
            if (publishDate != null) {
                book.setPublishDate(publishDate);
            }
            
            if (price != null) {
                book.updatePrice(price);
            }
            
            book.updateStock(stockQuantity);
            
            return book;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map entity to domain", e);
        }
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
