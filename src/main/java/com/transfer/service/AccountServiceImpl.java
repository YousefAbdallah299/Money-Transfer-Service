package com.transfer.service;


import com.transfer.dto.CreateAccountDTO;
import com.transfer.dto.ReturnAccountDTO;
import com.transfer.dto.UpdateAccountDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;


    @Override
    public ReturnAccountDTO createAccount(CreateAccountDTO createAccountDTO) throws ResourceNotFoundException,AccountCurrencyAlreadyExistsException{
        Customer customer = this.customerRepository.findById(createAccountDTO.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not Found!")
        );

        if(Boolean.TRUE.equals(customer.getAccounts().stream().anyMatch(account -> account.getCurrency().equals(createAccountDTO.getCurrency()))))
            throw new AccountCurrencyAlreadyExistsException("This Customer Already Has An Account With This Currency!");

        Account account = Account.builder()
                .balance(0.0)
                .accountType(createAccountDTO.getAccountType())
                .accountDescription(createAccountDTO.getAccountDescription())
                .accountName(createAccountDTO.getAccountName())
                .currency(createAccountDTO.getCurrency())
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .customer(customer)
                .build();

        accountRepository.save(account);

        return account.toDTO();
    }

    @Override
    public ReturnAccountDTO getAccountById(Long accountId) throws ResourceNotFoundException{
        return checkAccountExistance(accountId).toDTO();
    }

    @Override
    public ReturnAccountDTO updateAccount(UpdateAccountDTO accountDTO) throws ResourceNotFoundException {
        Account account = checkAccountExistance(accountDTO.getAccountId());
        account.setAccountName(accountDTO.getAccountName());
        account.setAccountDescription(accountDTO.getAccountDescription());
        account.setAccountNumber(account.getAccountNumber());
        accountRepository.save(account);
        return account.toDTO();
    }

    @Override
    @Transactional
    public void deleteAccount(Long accountId) throws ResourceNotFoundException {
        Account account = checkAccountExistance(accountId);

        Customer customer = account.getCustomer();
        customer.getAccounts().remove(account);

        customerRepository.save(customer);
        accountRepository.deleteById(accountId);
    }

    @Override
    public void deposit(Long accountId, Double amount) throws ResourceNotFoundException{
        Account account = checkAccountExistance(accountId);
        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);
    }

    private Account checkAccountExistance(Long id) throws ResourceNotFoundException{
        return this.accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account Not Found!")
        );
    }
}
