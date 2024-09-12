package com.transfer.service;
import com.transfer.dto.enums.AccountCurrency;
import com.transfer.dto.enums.AccountType;
import com.transfer.dto.request.CreateAccountRequestDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setEmail("test@example.com");

        account = new Account();
        account.setAccountNumber("1234567890");
        account.setCustomer(customer);
        account.setBalance(100.0);
    }

    @Test
    public void testCreateAccount() throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException {
        CreateAccountRequestDTO createAccountRequestDTO = new CreateAccountRequestDTO();
        createAccountRequestDTO.setAccountType(AccountType.SAVINGS);
        createAccountRequestDTO.setAccountDescription("Test account");
        createAccountRequestDTO.setAccountName("Test account");
        createAccountRequestDTO.setCurrency(AccountCurrency.EGP);

        when(customerRepository.findUserByEmail(any())).thenReturn(Optional.of(customer));
        when(accountRepository.save(any())).thenReturn(account);

        accountService.createAccount(createAccountRequestDTO, customer.getEmail());

        verify(accountRepository, times(1)).save(any());
    }
    @Test
    public void testCreateAccount_AccountCurrencyAlreadyExistsException() throws ResourceNotFoundException, AccountCurrencyAlreadyExistsException {
        CreateAccountRequestDTO createAccountRequestDTO = new CreateAccountRequestDTO();
        createAccountRequestDTO.setAccountType(AccountType.SAVINGS);
        createAccountRequestDTO.setAccountDescription("Test account");
        createAccountRequestDTO.setAccountName("Test account");
        createAccountRequestDTO.setCurrency(AccountCurrency.EGP);

        when(customerRepository.findUserByEmail(any())).thenReturn(Optional.of(customer));
        when(accountRepository.save(any())).thenThrow(new AccountCurrencyAlreadyExistsException("Account currency already exists"));

        assertThrows(AccountCurrencyAlreadyExistsException.class, () -> {
            accountService.createAccount(createAccountRequestDTO, customer.getEmail());
        });
    }



    @Test
    public void testDeleteAccount() throws ResourceNotFoundException, UnauthorizedAccessException {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        accountService.deleteAccount(account.getAccountNumber(), customer.getEmail());

        verify(accountRepository, times(1)).deleteById(any());
    }


    @Test
    public void testDeleteAccount_ResourceNotFoundException() throws ResourceNotFoundException, UnauthorizedAccessException {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deleteAccount(account.getAccountNumber(), customer.getEmail());
        });
    }



    @Test
    public void testDeposit() throws ResourceNotFoundException, UnauthorizedAccessException {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        accountService.deposit(account.getAccountNumber(), 100.0, customer.getEmail());

        assertEquals(200.0, account.getBalance());
    }



    @Test
    public void testWithdraw() throws ResourceNotFoundException, InsufficientFundsException, UnauthorizedAccessException {
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        accountService.withdraw(account.getId(), 50.0, customer.getEmail());

        assertEquals(50.0, account.getBalance());
    }

    @Test
    public void testWithdraw_InsufficientFundsException() throws ResourceNotFoundException, InsufficientFundsException, UnauthorizedAccessException {
        account.setBalance(30.0);
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.withdraw(account.getId(), 50.0, customer.getEmail());
        });
    }


}