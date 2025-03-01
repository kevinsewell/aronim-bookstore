package com.aronim.bookstore.domain.model;

import lombok.Value;

import java.util.UUID;

@Value
public class BookId {
    UUID value;

    public BookId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
