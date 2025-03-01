package com.aronim.bookstore.application.dto;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class UserDTO {
    UUID id;
    String email;
    String firstName;
    String lastName;
    LocalDateTime createdAt;
    LocalDateTime lastLoginAt;
    String status;
}
