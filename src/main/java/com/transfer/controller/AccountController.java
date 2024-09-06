package com.transfer.controller;

import com.transfer.dto.CreateAccountDTO;
import com.transfer.dto.ReturnAccountDTO;
import com.transfer.dto.ReturnTransactionDTO;
import com.transfer.dto.UpdateAccountDTO;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.service.AccountService;
import com.transfer.service.security.JwtUtils;
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
public class AccountController {
    private final AccountService accountService;

    private final JwtUtils jwtUtils;


    @PostMapping("/create")
    public ResponseEntity<ReturnAccountDTO> createAccount(@RequestBody @Valid CreateAccountDTO createAccountDTO,@RequestHeader("Authorization") String token) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException {

        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(accountService.createAccount(createAccountDTO,loggedInUserEmail), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnAccountDTO> getAccountByID(@PathVariable(value = "id") Long accountid, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {


        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        return new ResponseEntity<>(accountService.getAccountById(accountid,loggedInUserEmail), HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<ReturnAccountDTO> updateAccount(@RequestBody @Valid UpdateAccountDTO accountDTO,@RequestHeader("Authorization") String token) throws ResourceNotFoundException,UnauthorizedAccessException{


        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        return new ResponseEntity<>(accountService.updateAccount(accountDTO,loggedInUserEmail), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(value = "id") Long accountId,@RequestHeader("Authorization") String token) throws ResourceNotFoundException ,UnauthorizedAccessException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        accountService.deleteAccount(accountId,loggedInUserEmail);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/deposit/{id}/{amount}")
   public ResponseEntity<Void> deposit(@PathVariable(value = "id") Long accountId,@PathVariable(value = "amount") Double amount,@RequestHeader("Authorization") String token) throws ResourceNotFoundException ,UnauthorizedAccessException{

        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        accountService.deposit(accountId,amount,loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{accountid}")
    public ResponseEntity<Double> getBalance(@PathVariable(value = "accountid") Long accountID,@RequestHeader("Authorization") String token) throws ResourceNotFoundException ,UnauthorizedAccessException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(accountService.getBalance(accountID,loggedInUserEmail), HttpStatus.OK);
    }


    @GetMapping("{accountid}/transactions")
    public ResponseEntity<Set<ReturnTransactionDTO>> getTransactions(@PathVariable (value = "accountid") Long accountID,@RequestHeader("Authorization") String token) throws ResourceNotFoundException,UnauthorizedAccessException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(accountService.getTransactions(accountID,loggedInUserEmail),HttpStatus.OK);
    }
}
