package com.transfer.service;

import com.transfer.dto.CustomerDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"))
                .toDTO();
    }
}