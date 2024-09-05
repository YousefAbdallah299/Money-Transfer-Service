package com.transfer.service;

import com.transfer.dto.CustomerDTO;
import com.transfer.exception.custom.InsufficientFundsException;
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


    /**
     * transfer money from an account to another
     *
     * @param senderID for the sender id
     * @param recieverID for the receiver id
     * @param amount for the amount to be transferred
     * @throws ResourceNotFoundException if the reciever or sender are not found
     * @throws InsufficientFundsException if the sender doesn't have enough money
     */
    void transfer(Long senderID, Long recieverID, Double amount) throws ResourceNotFoundException, InsufficientFundsException;
}