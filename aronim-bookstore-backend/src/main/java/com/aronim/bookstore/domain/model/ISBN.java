package com.aronim.bookstore.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ISBN {
    private final String value;

    public ISBN(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or blank");
        }
        if (!isValidISBN(value)) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        this.value = value;
    }

    private boolean isValidISBN(String isbn) {
        // Simplified validation: ISBN-10 or ISBN-13 format
        String cleanISBN = isbn.replaceAll("[\\s-]", "");
        return (cleanISBN.length() == 10 || cleanISBN.length() == 13) &&
                cleanISBN.matches("\\d{9}[\\dX]|\\d{13}");
    }
}
