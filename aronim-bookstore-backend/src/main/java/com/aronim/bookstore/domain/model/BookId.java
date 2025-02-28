package com.aronim.bookstore.domain.model;

import java.util.UUID;

public class BookId {
    private final UUID value;

    public BookId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookId bookId = (BookId) o;
        return value.equals(bookId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
