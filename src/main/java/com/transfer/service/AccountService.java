package com.transfer.service;

import com.transfer.dto.CreateAccountDTO;
import com.transfer.dto.ReturnAccountDTO;
import com.transfer.dto.ReturnTransactionDTO;
import com.transfer.dto.UpdateAccountDTO;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;

import java.util.Set;

public interface AccountService {


    /**
     * Create a new account
     *
     * @param createAccountDTO the account to be created
     * @return the created account
     * @throws ResourceNotFoundException if the account is not found
     */
    ReturnAccountDTO createAccount(CreateAccountDTO createAccountDTO,String loggedInUserEmail) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException;


    /**
     * Get account by id
     *
     * @param accountId the account id
     * @return the account
     * @throws ResourceNotFoundException if the account is not found
     */
    ReturnAccountDTO getAccountById(Long accountId, String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException;

    ReturnAccountDTO updateAccount(UpdateAccountDTO accountDTO, String loggedInUserEmail)throws ResourceNotFoundException,UnauthorizedAccessException;

    void deleteAccount(Long accountId, String loggedInUserEmail)throws ResourceNotFoundException, UnauthorizedAccessException;

    void deposit(Long accountId, Double amount,String loggedInUserEmail)throws ResourceNotFoundException,UnauthorizedAccessException;

    void withdraw(Long accountId, Double amount,String loggedInUserEmail)throws ResourceNotFoundException, InsufficientFundsException,UnauthorizedAccessException;


    Double getBalance(Long accountID,String loggedInUserEmail)throws ResourceNotFoundException,UnauthorizedAccessException;

    Set<ReturnTransactionDTO> getTransactions(Long accountID,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException;




}
