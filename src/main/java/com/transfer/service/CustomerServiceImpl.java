package com.transfer.service;

import com.transfer.dto.request.FavoritesRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.entity.FavRecipient;
import com.transfer.entity.key.FavRecipientId;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.UnauthorizedAccessException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import com.transfer.repository.FavoriteRecipientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final FavoriteRecipientRepository favoriteRecipientRepository;


    @Override
    public List<AccountResponseDTO> getAccounts(String loggedInUserEmail) throws ResourceNotFoundException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");

        }

        return customer.getAccounts().stream()
                .map(Account::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<FavoritesResponseDTO> getFavorites(String loggedInUserEmail) throws UnauthorizedAccessException {
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
    @Transactional
    public void addFavorite(FavoritesRequestDTO FavoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Account account = accountRepository.findById(FavoritesRequestDTO.getRecipientAccountId())
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
    public void deleteFavorite(FavoritesRequestDTO FavoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Customer customer = customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Account account = accountRepository.findById(FavoritesRequestDTO.getRecipientAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        FavRecipientId favRecipientId = new FavRecipientId(customer.getId(), account.getId());

        FavRecipient favRecipient = favoriteRecipientRepository.findById(favRecipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite recipient not found"));

        customer.getFavoriteRecipients().remove(favRecipient);
        favoriteRecipientRepository.deleteById(favRecipientId);
    }



}