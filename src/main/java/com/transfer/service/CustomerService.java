package com.transfer.service;

import com.transfer.dto.request.EditCustomerDTO;
import com.transfer.dto.request.FavoritesRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.CustomerResponseDTO;
import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing customer-related operations.
 */
public interface CustomerService {

    /**
     * Retrieves the list of accounts associated with a customer.
     *
     * @param loggedInUserEmail the email of the logged-in user
     * @return a list of {@link AccountResponseDTO} representing the accounts of the user
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to access this data
     * @throws ResourceNotFoundException if the logged-in customer cannot be found
     */
    List<AccountResponseDTO> getAccounts(String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Retrieves the list of favorite recipients for a customer.
     *
     * @param loggedInUserEmail the email of the logged-in user
     * @return a set of {@link FavoritesResponseDTO} representing the favorite recipients of the user
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to access this data
     * @throws ResourceNotFoundException if the logged-in customer cannot be found
     */
    Set<FavoritesResponseDTO> getFavorites(String loggedInUserEmail) throws UnauthorizedAccessException, ResourceNotFoundException;

    /**
     * Adds a recipient to the customer's list of favorite recipients.
     *
     * @param favoritesRequestDTO the details of the recipient to be added to favorites
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the recipient or customer is not found
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to perform this action
     */
    void addFavorite(@RequestBody @Valid FavoritesRequestDTO favoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Removes a recipient from the customer's list of favorite recipients.
     *
     * @param favoritesRequestDTO the details of the recipient to be removed from favorites
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the recipient or customer is not found
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to perform this action
     */
    void deleteFavorite(@RequestBody @Valid FavoritesRequestDTO favoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Retrieves the information of the customer.
     *
     * @param loggedInUserEmail the email of the logged-in user
     * @return a {@link CustomerResponseDTO} representing the customer's information
     * @throws ResourceNotFoundException if the logged-in customer cannot be found
     */
    CustomerResponseDTO getCustomerInfo(String loggedInUserEmail) throws ResourceNotFoundException;

    /**
     * Updates the information of the customer.
     *
     * @param loggedInUserEmail the email of the logged-in user
     * @param editCustomerDTO the details to be updated
     * @return a {@link CustomerResponseDTO} representing the updated customer's information
     * @throws ResourceNotFoundException if the logged-in customer cannot be found
     */
    CustomerResponseDTO editCustomerInfo(String loggedInUserEmail, EditCustomerDTO editCustomerDTO) throws ResourceNotFoundException;
}
