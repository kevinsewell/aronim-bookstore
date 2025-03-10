package com.aronim.bookstore.catalog.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Title {
    String value;

    public Title(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("Title cannot exceed 200 characters");
        }
        this.value = value;
    }
}
