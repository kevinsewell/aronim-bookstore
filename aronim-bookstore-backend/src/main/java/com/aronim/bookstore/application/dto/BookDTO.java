package com.aronim.bookstore.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
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
}
