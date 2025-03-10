package com.aronim.bookstore.catalog.infrastructure.persistence.entity;

import com.aronim.bookstore.catalog.domain.model.BookStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
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

    @Column(name = "publisher_name", nullable = false)
    private String publisherName;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status;

    // Default constructor for JPA
    protected BookEntity() {
    }
}
