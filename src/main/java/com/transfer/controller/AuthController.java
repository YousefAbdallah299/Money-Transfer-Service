package com.transfer.controller;

import com.transfer.dto.request.LoginRequestDTO;
import com.transfer.dto.response.LoginResponseDTO;
import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.dto.response.RegisterCustomerResponseDTO;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.service.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Endpoints for customer authentication and authorization, including registration, login, and logout.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new customer", description = "Creates a new customer account with the provided details. An email address must be unique.")
    public ResponseEntity<RegisterCustomerResponseDTO> register(@RequestBody @Valid RegisterCustomerRequestDTO customer) throws EmailAlreadyExistsException {
        return new ResponseEntity<>(this.authService.register(customer), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password", description = "Authenticates a customer by email and password, and returns a JWT token upon successful login.")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(this.authService.login(loginRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout the user", description = "Invalidates the JWT token for the currently logged-in customer, logging them out of the system.")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}
