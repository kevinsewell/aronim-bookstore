package com.aronim.bookstore.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class BookId {
    private final UUID value;

    public BookId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        this.value = value;
    }
}
