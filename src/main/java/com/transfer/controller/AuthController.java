package com.transfer.controller;

import com.transfer.dto.LoginRequestDTO;
import com.transfer.dto.LoginResponseDTO;
import com.transfer.dto.RegisterCustomerRequest;
import com.transfer.dto.RegisterCustomerResponse;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.service.security.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterCustomerResponse> register(@RequestBody @Valid RegisterCustomerRequest customer) throws EmailAlreadyExistsException {
        return new ResponseEntity<>(this.authService.register(customer), HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>  login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        return new ResponseEntity<>(this.authService.login(loginRequestDTO), HttpStatus.OK);

    }

    @PostMapping("/logout")

    public ResponseEntity<Void> login(@RequestHeader("Authorization") String token){
        token = token.substring(7);
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

}
