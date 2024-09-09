package com.transfer.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.dto.request.CreateAccountRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.TransactionPageResponseDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.entity.Transaction;
import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import com.transfer.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final TransactionRepository transactionRepository;

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";


    @Override
    public AccountResponseDTO createAccount(CreateAccountRequestDTO createAccountRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException,AccountCurrencyAlreadyExistsException{
        Customer customer = this.customerRepository.findUserByEmail(loggedInUserEmail).orElseThrow(
                () -> new ResourceNotFoundException("Customer Not Found!")
        );



        if(Boolean.TRUE.equals(customer.getAccounts().stream().anyMatch(account -> account.getCurrency().equals(createAccountRequestDTO.getCurrency()))))
            throw new AccountCurrencyAlreadyExistsException("This Customer Already Has An Account With This Currency!");

        Account account = Account.builder()
                .balance(0.0)
                .accountType(createAccountRequestDTO.getAccountType())
                .accountDescription(createAccountRequestDTO.getAccountDescription())
                .accountName(createAccountRequestDTO.getAccountName())
                .currency(createAccountRequestDTO.getCurrency())
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .customer(customer)
                .build();

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
//    @Cacheable(value = "transactions",key = "#pageNo")
    public TransactionPageResponseDTO getTransactions(Long accountID, String loggedInUserEmail,Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException,UnauthorizedAccessException {
        Account account = checkAccountExistance(accountID);
        if (!account.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Transaction> transactionPage =  transactionRepository.getUserTransactionsHistoryByAccountId(accountID, pageable);



        boolean isLast = pageNo >= transactionPage.getTotalPages() - 1;

        return TransactionPageResponseDTO.builder()
                .totalElement(transactionPage.getNumberOfElements())
                .totalPages(transactionPage.getTotalPages())
                .pageNumber(pageNo)
                .pageSize(pageSize)
                .isLast(isLast)
                .transactionsForAccount(
                        transactionPage.getContent().
                                stream()
                                .map(Transaction::toDTO).toList()
                )
                .build();

    }




    @Override
    @Transactional
//    @CachePut(cacheNames = {"studentCache"}, key="#student.studentId")
    public void transfer(Long senderID, Long receiverID, Double amount, String loggedInUserEmail) throws ResourceNotFoundException, InsufficientFundsException, IOException {
        Account sender = accountRepository.findById(senderID)
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        Account reciever = accountRepository.findById(receiverID)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        this.withdraw(senderID,amount,loggedInUserEmail);

        this.deposit(receiverID,convertCurrency(sender.getCurrency().toString(),reciever.getCurrency().toString(),amount),reciever.getCustomer().getEmail());

        Transaction transaction = Transaction.builder()
                .account(sender)
                .createdAt(LocalDateTime.now())
                .amountTransferred(amount)
                .currency(sender.getCurrency())
                .recieverID(receiverID)
                .build();

        sender.getTransactions().add(transaction);

        transactionRepository.save(transaction);

    }



    private double convertCurrency(String fromCurrency, String toCurrency, double amount) throws IOException {
        String url = API_URL + fromCurrency;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            double exchangeRate = rootNode.path("rates").path(toCurrency).asDouble();

            return amount * exchangeRate;
        }
    }



    private Account checkAccountExistance(Long id) throws ResourceNotFoundException{
        return this.accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account Not Found!")
        );
    }
}
