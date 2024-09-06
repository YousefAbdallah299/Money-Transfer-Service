package com.transfer.service;

import com.transfer.dto.AddFavoritesDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.ReturnFavoritesDTO;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

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
    void transfer(Long senderID, Long recieverID, Double amount, String loggedInUserEmail) throws ResourceNotFoundException, InsufficientFundsException,UnauthorizedAccessException;

    Set<ReturnFavoritesDTO> getFavorites(String loggedInUserEmail)throws UnauthorizedAccessException;

    void addFavorite(@RequestBody @Valid AddFavoritesDTO addFavoritesDTO,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException;

    void deleteFavorite(@RequestBody @Valid AddFavoritesDTO addFavoritesDTO,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException;

}