package com.transfer.controller;

import com.transfer.dto.request.CreateAccountRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
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

import java.util.Set;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Accounts", description = "Endpoints for managing customer accounts, including creation, updating, and retrieving account details.")
public class AccountController {
    private final AccountService accountService;
    private final JwtUtils jwtUtils;

    @PostMapping("/create")
    @Operation(summary = "Create a new account", description = "Creates a new account for the logged-in customer. The account must have a unique currency.")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid CreateAccountRequestDTO createAccountRequestDTO, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(accountService.createAccount(createAccountRequestDTO, loggedInUserEmail), HttpStatus.CREATED);
    }



    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete an account by ID", description = "Deletes the specified account ID for the logged-in customer.")
    public ResponseEntity<Void> deleteAccount(@PathVariable(value = "id") Long accountId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        accountService.deleteAccount(accountId, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deposit/{id}/{amount}")
    @Operation(summary = "Deposit money into an account", description = "Deposits a specified amount of money into the specified account for the logged-in customer.")
    public ResponseEntity<Void> deposit(@PathVariable(value = "id") Long accountId, @PathVariable(value = "amount") Double amount, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        accountService.deposit(accountId, amount, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{accountid}")
    @Operation(summary = "Get the balance of an account", description = "Retrieves the current balance of the specified account for the logged-in customer.")
    public ResponseEntity<Double> getBalance(@PathVariable(value = "accountid") Long accountID, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(accountService.getBalance(accountID, loggedInUserEmail), HttpStatus.OK);
    }

    @GetMapping("{accountid}/transactions")
    @Operation(summary = "Get all transactions of an account", description = "Fetches all the transactions of the specified account for the logged-in customer.")
    public ResponseEntity<Set<TransactionResponseDTO>> getTransactions(@PathVariable(value = "accountid") Long accountID, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(accountService.getTransactions(accountID, loggedInUserEmail), HttpStatus.OK);
    }



    @PutMapping("/transfer/{senderID}/{receiverAccountId}/{amount}")
    @Operation(summary = "Transfer money between accounts", description = "Transfers a specified amount from the sender's account to the receiver's account.")
    public ResponseEntity<Void> transfer(
            @RequestHeader("Authorization") String token,
            @PathVariable(value = "senderID") Long senderID,
            @PathVariable(value = "receiverAccountId") Long receiverID,
            @PathVariable(value = "amount") Double amount
    ) throws ResourceNotFoundException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        accountService.transfer(senderID, receiverID, amount, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }



}
