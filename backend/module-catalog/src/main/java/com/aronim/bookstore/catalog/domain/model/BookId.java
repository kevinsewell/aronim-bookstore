package com.aronim.bookstore.catalog.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
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
