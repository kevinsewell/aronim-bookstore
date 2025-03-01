package com.aronim.bookstore.presentation.controller;

import com.aronim.bookstore.application.dto.UserDTO;
import com.aronim.bookstore.application.service.UserService;
import com.aronim.bookstore.presentation.request.ChangePasswordRequest;
import com.aronim.bookstore.presentation.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for handling user-related operations.
 * <p>
 * This controller provides endpoints for creating users, retrieving user information,
 * and managing user passwords in the bookstore system. It handles HTTP requests and
 * delegates business logic to the {@link UserService}.
 * </p>
 * <p>
 * Available operations:
 * <ul>
 *   <li>Create a new user</li>
 *   <li>Retrieve a user by ID</li>
 *   <li>Retrieve a user by email</li>
 *   <li>Change a user's password</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations for managing users in the bookstore")
public class UserController {
    private final UserService userService;

    /**
     * Constructs a new UserController with the specified user service.
     *
     * @param userService the service to handle user-related business logic
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user in the system.
     *
     * @param request the data transfer object containing user details
     * @return a ResponseEntity containing the created UserDTO with HTTP status 201 (CREATED)
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User with the same email already exists")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the UUID of the user to retrieve
     * @return a ResponseEntity containing the UserDTO with HTTP status 200 (OK) if found,
     * or HTTP status 404 (NOT_FOUND) if no user exists with the given ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieves a user using their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return a ResponseEntity containing the UserDTO with HTTP status 200 (OK) if found,
     * or HTTP status 404 (NOT_FOUND) if no user exists with the given email
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get a user by email", description = "Retrieves a user using their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Changes a user's password.
     *
     * @param id      the UUID of the user whose password will be changed
     * @param request the data transfer object containing the new password
     * @return a ResponseEntity with HTTP status 200 (OK) if the update was successful
     */
    @PatchMapping("/{id}/password")
    @Operation(summary = "Change user password", description = "Updates a user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password value"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> changePassword(@PathVariable UUID id,
                                              @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
