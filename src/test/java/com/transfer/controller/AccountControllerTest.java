package com.transfer.controller;

import com.transfer.dto.request.CreateAccountRequestDTO;
import com.transfer.dto.request.TransferRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.TransactionResponseDTO;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.AccountService;
import com.transfer.service.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

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
    void createAccount_ShouldReturnCreatedAccount() {
        CreateAccountRequestDTO request = new CreateAccountRequestDTO();
        AccountResponseDTO response = new AccountResponseDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(accountService.createAccount(any(CreateAccountRequestDTO.class), eq(email))).thenReturn(response);

        ResponseEntity<AccountResponseDTO> result = accountController.createAccount(request, token);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(accountService).createAccount(request, email);
    }

    @Test
    void deleteAccount_ShouldReturnOk() {
        String accountNumber = "123456";

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);

        ResponseEntity<Void> result = accountController.deleteAccount(accountNumber, token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(accountService).deleteAccount(accountNumber, email);
    }

    @Test
    void deleteAccount_ShouldThrowResourceNotFoundException() {
        String accountNumber = "123456";

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        doThrow(new ResourceNotFoundException("Account not found")).when(accountService).deleteAccount(accountNumber, email);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.deleteAccount(accountNumber, token);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountService).deleteAccount(accountNumber, email);
    }

    @Test
    void deposit_ShouldReturnOk() {
        String accountNumber = "123456";
        Double amount = 100.0;

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);

        ResponseEntity<Void> result = accountController.deposit(accountNumber, amount, token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(accountService).deposit(accountNumber, amount, email);
    }

    @Test
    void deposit_ShouldThrowResourceNotFoundException() {
        String accountNumber = "123456";
        Double amount = 100.0;

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        doThrow(new ResourceNotFoundException("Account not found")).when(accountService).deposit(accountNumber, amount, email);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.deposit(accountNumber, amount, token);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountService).deposit(accountNumber, amount, email);
    }

    @Test
    void getBalance_ShouldReturnBalance() {
        String accountNumber = "123456";
        Double balance = 500.0;

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(accountService.getBalance(any(String.class), eq(email))).thenReturn(balance);

        ResponseEntity<Double> result = accountController.getBalance(accountNumber, token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(balance, result.getBody());
        verify(accountService).getBalance(accountNumber, email);
    }

    @Test
    void getBalance_ShouldThrowResourceNotFoundException() {
        String accountNumber = "123456";

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        doThrow(new ResourceNotFoundException("Account not found")).when(accountService).getBalance(any(String.class), eq(email));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.getBalance(accountNumber, token);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountService).getBalance(accountNumber, email);
    }

    @Test
    void transfer_ShouldReturnTransactionResponse() throws IOException {
        TransferRequestDTO request = new TransferRequestDTO();
        TransactionResponseDTO response = new TransactionResponseDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(accountService.transfer(any(TransferRequestDTO.class), eq(email))).thenReturn(response);

        ResponseEntity<TransactionResponseDTO> result = accountController.transfer(token, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(accountService).transfer(request, email);
    }

    @Test
    void transfer_ShouldThrowInsufficientFundsException() throws IOException {
        TransferRequestDTO request = new TransferRequestDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        doThrow(new InsufficientFundsException("Insufficient funds")).when(accountService).transfer(any(TransferRequestDTO.class), eq(email));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            accountController.transfer(token, request);
        });

        assertEquals("Insufficient funds", exception.getMessage());
        verify(accountService).transfer(request, email);
    }
}
