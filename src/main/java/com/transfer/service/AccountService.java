package com.transfer.service;

import com.transfer.dto.CreateAccountDTO;
import com.transfer.dto.ReturnAccountDTO;
import com.transfer.dto.UpdateAccountDTO;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;

public interface AccountService {


    /**
     * Create a new account
     *
     * @param createAccountDTO the account to be created
     * @return the created account
     * @throws ResourceNotFoundException if the account is not found
     */
    ReturnAccountDTO createAccount(CreateAccountDTO createAccountDTO) throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException;


    /**
     * Get account by id
     *
     * @param accountId the account id
     * @return the account
     * @throws ResourceNotFoundException if the account is not found
     */
    ReturnAccountDTO getAccountById(Long accountId) throws ResourceNotFoundException;

    ReturnAccountDTO updateAccount(UpdateAccountDTO accountDTO)throws ResourceNotFoundException;

    void deleteAccount(Long accountId)throws ResourceNotFoundException;

    void deposit(Long accountId, Double amount)throws ResourceNotFoundException;


}
