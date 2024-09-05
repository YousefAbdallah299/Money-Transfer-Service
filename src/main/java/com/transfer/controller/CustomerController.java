package com.transfer.controller;


import com.transfer.dto.CustomerDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable Long customerId) throws ResourceNotFoundException {
        return this.customerService.getCustomerById(customerId);
    }

    @PutMapping("/transfer/{senderAccountId}/{receiverAccountId}/{amount}")
    public ResponseEntity<Void> transfer(@PathVariable(value = "senderAccountId") Long senderID,@PathVariable(value = "receiverAccountId") Long receiverID, @PathVariable(value = "amount") Double amount) throws ResourceNotFoundException{
        this.customerService.transfer(senderID,receiverID, amount);
        return ResponseEntity.ok().build();
    }
}