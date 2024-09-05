package com.transfer.service;

import com.transfer.dto.CustomerDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Transaction;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import com.transfer.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final TransactionRepository transactionRepository;

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"))
                .toDTO();
    }


    @Override
    @Transactional
    public void transfer(Long senderID, Long recieverID, Double amount) throws ResourceNotFoundException, InsufficientFundsException {
        Account sender = accountRepository.findById(senderID)
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        accountRepository.findById(recieverID)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        accountService.withdraw(senderID,amount);
        accountService.deposit(recieverID,amount);

        Transaction transaction = Transaction.builder()
                .account(sender)
                .createdAt(LocalDateTime.now())
                .amountTransferred(amount)
                .currency(sender.getCurrency())
                .recieverID(recieverID)
                .build();

        sender.getTransactions().add(transaction);

        transactionRepository.save(transaction);



    }


}