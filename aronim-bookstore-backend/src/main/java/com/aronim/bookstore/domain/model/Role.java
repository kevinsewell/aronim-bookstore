package com.aronim.bookstore.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Role {
    private RoleId id;
    private String name;
    private String description;

    private Role() {
        // Private constructor for factory method
    }

    public static Role create(RoleId id, String name, String description) {
        Role role = new Role();
        role.id = id;
        role.name = name;
        role.description = description;
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
