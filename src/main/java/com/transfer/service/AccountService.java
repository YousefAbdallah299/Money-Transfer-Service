package com.transfer.service;

import com.transfer.dto.request.CreateAccountRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.TransactionResponseDTO;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Set;

public interface AccountService {

    /**
     * Create a new account
     *
     * @param createAccountRequestDTO the account to be created
     * @param loggedInUserEmail the email of the logged-in user
     * @return the created account
     * @throws ResourceNotFoundException if the account is not found
     * @throws AccountCurrencyAlreadyExistsException if the user already has an account with the same currency
     */
    AccountResponseDTO createAccount(@RequestBody @Valid CreateAccountRequestDTO createAccountRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException;



    /**
     * Delete an account by id
     *
     * @param accountId the account id
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the account is not found
     * @throws UnauthorizedAccessException if the user is not authorized to delete this account
     */
    void deleteAccount(Long accountId, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Deposit money into an account
     *
     * @param accountId the account id
     * @param amount the amount to deposit
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the account is not found
     * @throws UnauthorizedAccessException if the user is not authorized to deposit into this account
     */
    void deposit(Long accountId, Double amount, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Withdraw money from an account
     *
     * @param accountId the account id
     * @param amount the amount to withdraw
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the account is not found
     * @throws InsufficientFundsException if there are insufficient funds in the account
     * @throws UnauthorizedAccessException if the user is not authorized to withdraw from this account
     */
    void withdraw(Long accountId, Double amount, String loggedInUserEmail) throws ResourceNotFoundException, InsufficientFundsException, UnauthorizedAccessException;

    /**
     * Get the balance of an account
     *
     * @param accountId the account id
     * @param loggedInUserEmail the email of the logged-in user
     * @return the balance of the account
     * @throws ResourceNotFoundException if the account is not found
     * @throws UnauthorizedAccessException if the user is not authorized to access this account's balance
     */
    Double getBalance(Long accountId, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Get all transactions of an account
     *
     * @param accountId the account id
     * @param loggedInUserEmail the email of the logged-in user
     * @return a set of transactions associated with the account
     * @throws ResourceNotFoundException if the account is not found
     * @throws UnauthorizedAccessException if the user is not authorized to access this account's transactions
     */
    Set<TransactionResponseDTO> getTransactions(Long accountId, String loggedInUserEmail) throws ResourceNotFoundException, UnauthorizedAccessException;




    /**
     * Transfer money from one account to another
     *
     * @param senderID the ID of the sender
     * @param receiverID the ID of the receiver
     * @param amount the amount to be transferred
     * @param loggedInUserEmail the email of the logged-in user
     * @throws ResourceNotFoundException if the sender or receiver is not found
     * @throws InsufficientFundsException if the sender does not have enough money
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to perform this transaction
     */
    void transfer(Long senderID, Long receiverID, Double amount, String loggedInUserEmail) throws ResourceNotFoundException, InsufficientFundsException, UnauthorizedAccessException, IOException;

}
