package com.aronim.bookstore.presentation.controller;

import com.aronim.bookstore.application.dto.RoleDTO;
import com.aronim.bookstore.application.dto.UserDTO;
import com.aronim.bookstore.application.service.UserService;
import com.aronim.bookstore.presentation.request.CreateRoleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final UserService userService;

    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@RequestBody CreateRoleRequest request) {
        RoleDTO roleDTO = userService.createRole(request.getName(), request.getDescription());
        return new ResponseEntity<>(roleDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<RoleDTO>> getAllRoles() {
        Set<RoleDTO> roles = userService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{roleId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> assignRoleToUser(
            @PathVariable UUID roleId,
            @PathVariable UUID userId) {
        UserDTO userDTO = userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> removeRoleFromUser(
            @PathVariable UUID roleId,
            @PathVariable UUID userId) {
        UserDTO userDTO = userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userDTO.id")
    public ResponseEntity<Set<RoleDTO>> getUserRoles(@PathVariable UUID userId) {
        Set<RoleDTO> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
}
