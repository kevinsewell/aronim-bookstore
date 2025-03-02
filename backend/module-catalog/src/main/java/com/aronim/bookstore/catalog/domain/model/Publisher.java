package com.aronim.bookstore.catalog.domain.model;


import lombok.Value;

@Value
public class Publisher {
    String name;

    public Publisher(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Publisher name cannot be null or blank");
        }
        this.name = name;
    }
}
