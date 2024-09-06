package com.transfer.service;


import com.transfer.dto.CreateAccountDTO;
import com.transfer.dto.ReturnAccountDTO;
import com.transfer.dto.ReturnTransactionDTO;
import com.transfer.dto.UpdateAccountDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.entity.Transaction;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;


    @Override
    public ReturnAccountDTO createAccount(CreateAccountDTO createAccountDTO,String loggedInUserEmail) throws ResourceNotFoundException,AccountCurrencyAlreadyExistsException{
        Customer customer = this.customerRepository.findUserByEmail(loggedInUserEmail).orElseThrow(
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
    public ReturnAccountDTO getAccountById(Long accountId,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException{

        Account account = checkAccountExistance(accountId);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        return account.toDTO();
    }

    @Override
    public ReturnAccountDTO updateAccount(UpdateAccountDTO accountDTO,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException  {
        Account account = checkAccountExistance(accountDTO.getAccountId());
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }
        account.setAccountName(accountDTO.getAccountName());
        account.setAccountDescription(accountDTO.getAccountDescription());
        account.setAccountNumber(account.getAccountNumber());
        accountRepository.save(account);
        return account.toDTO();
    }

    @Override
    @Transactional
    public void deleteAccount(Long accountId,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Account account = checkAccountExistance(accountId);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Customer customer = account.getCustomer();
        customer.getAccounts().remove(account);

        customerRepository.save(customer);
        accountRepository.deleteById(accountId);
    }

    @Override
    public void deposit(Long accountId, Double amount,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException{
        Account account = checkAccountExistance(accountId);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }
        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);
    }

    @Override
    public void withdraw(Long accountId, Double amount,String loggedInUserEmail) throws ResourceNotFoundException,InsufficientFundsException,UnauthorizedAccessException{
        Account account = checkAccountExistance(accountId);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }
        if(Boolean.TRUE.equals(account.getBalance()>=amount)){
            account.setBalance(account.getBalance()-amount);
        }
        else{
            throw new InsufficientFundsException("Account doesn't have enough money");
        }
        accountRepository.save(account);
    }



    @Override
    public Double getBalance(Long accoundID,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException{
        Account account = checkAccountExistance(accoundID);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }
        return account.getBalance();
    }

    @Override
    public Set<ReturnTransactionDTO> getTransactions(Long accountID,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Account account = checkAccountExistance(accountID);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }
        return account.getTransactions().stream()
                .map(Transaction::toDTO)
                .collect(Collectors.toSet());
    }


    private Account checkAccountExistance(Long id) throws ResourceNotFoundException{
        return this.accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account Not Found!")
        );
    }
}
