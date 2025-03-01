// AuthResponse.java
package com.aronim.bookstore.presentation.response;

import lombok.Value;

import java.util.UUID;

@Value
public class AuthResponse {
    UUID id;
    String email;
    String firstName;
    String lastName;
    String message;
    // You can add a token field here if implementing JWT
}
