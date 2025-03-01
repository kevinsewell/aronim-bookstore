package com.aronim.bookstore.presentation.request;

import lombok.Data;

@Data
public class CreateRoleRequest {
    private String name;
    private String description;
}
