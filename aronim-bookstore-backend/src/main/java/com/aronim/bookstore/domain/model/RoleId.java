package com.aronim.bookstore.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class RoleId {
    private final UUID value;

    public RoleId(UUID value) {
        this.value = value;
    }

    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }
}
