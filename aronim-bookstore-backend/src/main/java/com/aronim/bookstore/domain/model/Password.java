package com.aronim.bookstore.domain.model;

import lombok.Value;

@Value
public class Password {
    String hashedValue;

    public Password(String hashedValue) {
        if (hashedValue == null || hashedValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty");
        }
        this.hashedValue = hashedValue;
    }
}
