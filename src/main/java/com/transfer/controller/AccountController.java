package com.transfer.controller;

import com.transfer.dto.request.CreateAccountRequestDTO;
import com.transfer.dto.request.TransferRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.TransactionPageResponseDTO;
import com.transfer.dto.response.TransactionResponseDTO;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.service.AccountService;
import com.transfer.service.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Accounts", description = "Endpoints for managing customer accounts, including creation, updating, and retrieving account details.")
public class AccountController {
    private final AccountService accountService;

    private final JwtUtils jwtUtils;



    @PostMapping("/create")
    @Operation(summary = "Create a new account", description = "Creates a new account for the logged-in customer. The account must have a unique currency.")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid CreateAccountRequestDTO createAccountRequestDTO, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException {
        String loggedInUserEmail = getEmailFromToken(token);
        return new ResponseEntity<>(accountService.createAccount(createAccountRequestDTO, loggedInUserEmail), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{accountNumber}")
    @Operation(summary = "Delete an account by account number", description = "Deletes the specified account for the logged-in customer.")
    public ResponseEntity<Void> deleteAccount(@PathVariable(value = "accountNumber") String accountNumber, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        String loggedInUserEmail = getEmailFromToken(token);
        accountService.deleteAccount(accountNumber, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deposit/{accountNumber}/{amount}")
    @Operation(summary = "Deposit money into an account", description = "Deposits a specified amount of money into the specified account for the logged-in customer.")
    public ResponseEntity<Void> deposit(@PathVariable(value = "accountNumber") String accountNumber, @PathVariable(value = "amount") Double amount, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        String loggedInUserEmail = getEmailFromToken(token);
        accountService.deposit(accountNumber, amount, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{accountNumber}")
    @Operation(summary = "Get the balance of an account", description = "Retrieves the current balance of the specified account for the logged-in customer.")
    public ResponseEntity<Double> getBalance(@PathVariable(value = "accountNumber") String accountNumber, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        String loggedInUserEmail = getEmailFromToken(token);
        return new ResponseEntity<>(accountService.getBalance(accountNumber, loggedInUserEmail), HttpStatus.OK);
    }

    @GetMapping("{accountNumber}/transactions")
    @Operation(summary = "Get all transactions of an account", description = "Fetches all the transactions of the specified account for the logged-in customer.")
    public ResponseEntity<TransactionPageResponseDTO> getTransactions(@PathVariable(value = "accountNumber") String accountNumber, @RequestHeader("Authorization") String token,
                                                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                                                      @RequestParam(defaultValue = "5") Integer pageSize,
                                                                      @RequestParam(defaultValue = "id") String sortBy
    ) throws ResourceNotFoundException, UnauthorizedAccessException {
        String loggedInUserEmail = getEmailFromToken(token);
        return new ResponseEntity<>(accountService.getTransactions(accountNumber, loggedInUserEmail,pageNo,pageSize,sortBy), HttpStatus.OK);
    }



    @PutMapping("/transfer")
    @Operation(summary = "Transfer money between accounts", description = "Transfers a specified amount from the sender's account to the receiver's account.")
    public ResponseEntity<TransactionResponseDTO> transfer(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid TransferRequestDTO transferRequestDTO
            ) throws ResourceNotFoundException, IOException {
        String loggedInUserEmail = getEmailFromToken(token);
        return new ResponseEntity<>(accountService.transfer(transferRequestDTO, loggedInUserEmail),HttpStatus.OK);

    }


    private String getEmailFromToken(String token) {
        return jwtUtils.getEmailFromJwtToken(token.substring(7));
    }


}
