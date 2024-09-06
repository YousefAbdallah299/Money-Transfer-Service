package com.transfer.service;

import com.transfer.dto.AddFavoritesDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.ReturnFavoritesDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.entity.FavRecipient;
import com.transfer.entity.Transaction;
import com.transfer.entity.key.FavRecipientId;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import com.transfer.repository.FavoriteRecipientRepository;
import com.transfer.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final TransactionRepository transactionRepository;

    private final FavoriteRecipientRepository favoriteRecipientRepository;

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"))
                .toDTO();
    }


    @Override
    @Transactional
    public void transfer(Long senderID, Long recieverID, Double amount, String loggedInUserEmail) throws ResourceNotFoundException, InsufficientFundsException {
        Account sender = accountRepository.findById(senderID)
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        Account reciever = accountRepository.findById(recieverID)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        accountService.withdraw(senderID,amount,loggedInUserEmail);
        accountService.deposit(recieverID,amount,reciever.getCustomer().getEmail());

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

    @Override
    public Set<ReturnFavoritesDTO> getFavorites(String loggedInUserEmail) throws UnauthorizedAccessException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        return customer.getFavoriteRecipients().stream()
                .map(FavRecipient::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public void addFavorite(AddFavoritesDTO AddFavoritesDTO,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Account account = accountRepository.findById(AddFavoritesDTO.getRecipientAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        FavRecipientId favRecipientId = new FavRecipientId(customer.getId(), account.getId());


        FavRecipient favRecipient = FavRecipient.builder()
                .id(favRecipientId)
                .customer(customer)
                .recipientAccount(account)
                .recipientName(account.getAccountName())
                .build();


        favoriteRecipientRepository.save(favRecipient);

    }

    @Override
    @Transactional
    public void deleteFavorite(AddFavoritesDTO AddFavoritesDTO,String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Customer customer = customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Account account = accountRepository.findById(AddFavoritesDTO.getRecipientAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        FavRecipientId favRecipientId = new FavRecipientId(customer.getId(), account.getId());

        FavRecipient favRecipient = favoriteRecipientRepository.findById(favRecipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite recipient not found"));

        customer.getFavoriteRecipients().remove(favRecipient);
        favoriteRecipientRepository.deleteById(favRecipientId);
    }



}