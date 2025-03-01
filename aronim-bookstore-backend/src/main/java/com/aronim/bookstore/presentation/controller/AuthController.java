package com.aronim.bookstore.presentation.controller;

import com.aronim.bookstore.application.dto.UserDTO;
import com.aronim.bookstore.application.service.UserService;
import com.aronim.bookstore.presentation.request.LoginRequest;
import com.aronim.bookstore.presentation.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Update last login time
        Optional<UserDTO> user = userService.findByEmail(loginRequest.getEmail());
        
        // For a simple implementation, we'll just return the user details
        // In a production app, you'd typically generate and return a JWT token here
        return user.map(userDTO -> ResponseEntity.ok(new AuthResponse(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                "Login successful"
        ))).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
