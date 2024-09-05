package com.transfer.controller;


import com.transfer.dto.CustomerDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}