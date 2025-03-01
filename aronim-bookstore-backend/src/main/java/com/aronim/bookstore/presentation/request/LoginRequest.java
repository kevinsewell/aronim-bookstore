// LoginRequest.java
package com.aronim.bookstore.presentation.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
