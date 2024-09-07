package com.transfer.service;

import com.transfer.dto.request.FavoritesRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

public interface CustomerService {
    /**
     * Get the list of accounts for a customer
     *
     * @param loggedInUserEmail the email of the logged-in user
     * @return a list of accounts of the user
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to access this data
     * @throws ResourceNotFoundException if the logged-in customer can't be found
     */

   List<AccountResponseDTO> getAccounts(String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException;
    /**
     * Get the list of favorite recipients for a customer
     *
     * @param loggedInUserEmail the email of the logged-in user
     * @return a set of favorite recipients
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to access this data
     * @throws ResourceNotFoundException if the logged-in customer can't be found
     */
    Set<FavoritesResponseDTO> getFavorites(String loggedInUserEmail) throws UnauthorizedAccessException,ResourceNotFoundException;

    /**
     * Add a recipient to the favorites list
     *
     * @param favoritesRequestDTO the details of the recipient to be added to favorites
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the recipient or customer is not found
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to perform this action
     */
    void addFavorite(@RequestBody @Valid FavoritesRequestDTO favoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Remove a recipient from the favorites list
     *
     * @param favoritesRequestDTO the details of the recipient to be removed from favorites
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the recipient or customer is not found
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to perform this action
     */
    void deleteFavorite(@RequestBody @Valid FavoritesRequestDTO favoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;
}
