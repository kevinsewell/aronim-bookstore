package com.aronim.bookstore.catalog.application.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class BookDTO {
    UUID id;
    String isbn;
    String title;
    String author;
    String publisher;
    LocalDate publishDate;
    BigDecimal price;
    int stockQuantity;
    String status;
}
