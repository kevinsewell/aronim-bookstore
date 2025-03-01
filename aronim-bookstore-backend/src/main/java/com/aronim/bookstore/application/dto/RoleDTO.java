package com.aronim.bookstore.application.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class RoleDTO {
    UUID id;
    String name;
    String description;
}
