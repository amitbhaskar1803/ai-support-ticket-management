package com.aiticket.controller;

import com.aiticket.dto.request.LoginRequest;
import com.aiticket.dto.request.RegisterRequest;
import com.aiticket.dto.response.AuthResponse;
import com.aiticket.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Public registration and login endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a user", description = "Public endpoint. New accounts are assigned the USER role.")
    @ApiResponse(responseCode = "201", description = "User registered")
    @ApiResponse(responseCode = "400", description = "Invalid input or email already registered")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in", description = "Public endpoint. Returns a JWT to use as a Bearer token on protected endpoints.")
    @ApiResponse(responseCode = "200", description = "Login successful; JWT returned")
    @ApiResponse(responseCode = "400", description = "Invalid credentials or input")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }
}
