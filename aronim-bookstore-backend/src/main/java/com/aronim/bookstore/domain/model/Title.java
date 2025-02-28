package com.aronim.bookstore.domain.model;

import lombok.Value;

@Value
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
