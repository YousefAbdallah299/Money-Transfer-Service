package com.transfer.controller;

import com.transfer.dto.request.ChangePasswordDTO;
import com.transfer.dto.request.LoginRequestDTO;
import com.transfer.dto.response.LoginResponseDTO;
import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.dto.response.CustomerResponseDTO;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.service.security.AuthService;
import com.transfer.service.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtils jwtUtils;

    private String token;
    private String email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        token = "Bearer someJwtToken";
        email = "user@example.com";
    }

    @Test
    void refresh_ShouldReturnOk() {
        ResponseEntity<Void> result = authController.refresh();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void register_ShouldReturnCreatedCustomer() throws EmailAlreadyExistsException {
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        CustomerResponseDTO response = new CustomerResponseDTO();

        when(authService.register(any(RegisterCustomerRequestDTO.class))).thenReturn(response);

        ResponseEntity<CustomerResponseDTO> result = authController.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService).register(request);
    }

    @Test
    void register_ShouldThrowEmailAlreadyExistsException() throws EmailAlreadyExistsException {
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();

        doThrow(new EmailAlreadyExistsException("Email already exists")).when(authService).register(any(RegisterCustomerRequestDTO.class));

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            authController.register(request);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(authService).register(request);
    }


    @Test
    void login_ShouldReturnLoginResponse() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        ResponseEntity<LoginResponseDTO> result = authController.login(loginRequestDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(loginResponseDTO, result.getBody());
        verify(authService).login(loginRequestDTO);
    }

    @Test
    void login_ShouldThrowResourceNotFoundException() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();

        doThrow(new ResourceNotFoundException("User not found")).when(authService).login(any(LoginRequestDTO.class));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authController.login(loginRequestDTO);
        });

        assertEquals("User not found", exception.getMessage());
        verify(authService).login(loginRequestDTO);
    }

    @Test
    void logout_ShouldReturnOk() {
        ResponseEntity<Void> result = authController.logout(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService).logout("someJwtToken");
    }

    @Test
    void logout_ShouldThrowUnauthorizedAccessException() {
        doThrow(new UnauthorizedAccessException("Unauthorized access")).when(authService).logout("someJwtToken");

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            authController.logout(token);
        });

        assertEquals("Unauthorized access", exception.getMessage());
        verify(authService).logout("someJwtToken");
    }

    @Test
    void changePassword_ShouldReturnOk() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);

        ResponseEntity<Void> result = authController.changePassword(token, changePasswordDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService).changePassword(changePasswordDTO, email);
    }

    @Test
    void changePassword_ShouldThrowUnauthorizedAccessException() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        doThrow(new UnauthorizedAccessException("Unauthorized access")).when(authService).changePassword(changePasswordDTO, email);

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            authController.changePassword(token, changePasswordDTO);
        });

        assertEquals("Unauthorized access", exception.getMessage());
        verify(authService).changePassword(changePasswordDTO, email);
    }
}