package com.aronim.bookstore.catalog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private UUID id;
    private String isbn;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String publisherName;
    private BigDecimal price;
    private int stockQuantity;
    private String status;
}
