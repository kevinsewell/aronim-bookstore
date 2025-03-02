package com.aronim.bookstore.catalog.infrastructure.persistence.entity;

import com.aronim.bookstore.catalog.domain.model.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "books")
public class BookEntity {
    // Getters and setters
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
    protected BookEntity() {
    }

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

}
