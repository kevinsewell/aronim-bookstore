package com.aronim.bookstore.domain.model;

import lombok.Value;
import java.util.UUID;

@Value
public class UserId {
    UUID value;

    public UserId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        this.value = value;
    }
}
