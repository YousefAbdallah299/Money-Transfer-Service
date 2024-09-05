package com.transfer.service;

import com.transfer.dto.CustomerDTO;
import com.transfer.exception.custom.ResourceNotFoundException;

public interface CustomerService {

    /**
     * Get customer by id
     *
     * @param customerId the customer id
     * @return the created customer
     * @throws ResourceNotFoundException if the customer is not found
     */
    CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException;
}