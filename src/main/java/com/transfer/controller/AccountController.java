package com.transfer.controller;

import com.transfer.dto.CreateAccountDTO;
import com.transfer.dto.ReturnAccountDTO;
import com.transfer.dto.ReturnTransactionDTO;
import com.transfer.dto.UpdateAccountDTO;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.AccountService;
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


    @PostMapping("/create")
    public ResponseEntity<ReturnAccountDTO> createAccount(@RequestBody @Valid CreateAccountDTO createAccountDTO) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException {

        return new ResponseEntity<>(accountService.createAccount(createAccountDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnAccountDTO> getAccountByID(@PathVariable(value = "id") Long accountid) throws ResourceNotFoundException {


        return new ResponseEntity<>(accountService.getAccountById(accountid), HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<ReturnAccountDTO> updateAccount(@RequestBody @Valid UpdateAccountDTO accountDTO) throws ResourceNotFoundException{
        return new ResponseEntity<>(accountService.updateAccount(accountDTO), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(value = "id") Long accountId) throws ResourceNotFoundException {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deposit/{id}/{amount}")
   public ResponseEntity<Void> deposit(@PathVariable(value = "id") Long accountId,@PathVariable(value = "amount") Double amount) throws ResourceNotFoundException {
        accountService.deposit(accountId,amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{accountid}")
    public ResponseEntity<Double> getBalance(@PathVariable(value = "accountid") Long accountID) throws ResourceNotFoundException {
        return new ResponseEntity<>(accountService.getBalance(accountID), HttpStatus.OK);
    }


    @GetMapping("{accountid}/transactions")
    public ResponseEntity<Set<ReturnTransactionDTO>> getTransactions(@PathVariable (value = "accountid") Long accountID) throws ResourceNotFoundException{
        return new ResponseEntity<>(accountService.getTransactions(accountID),HttpStatus.OK);
    }
}
