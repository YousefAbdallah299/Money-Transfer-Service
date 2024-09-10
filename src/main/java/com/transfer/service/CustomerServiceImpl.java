package com.transfer.service;

import com.transfer.dto.request.EditCustomerDTO;
import com.transfer.dto.request.FavoritesRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.CustomerResponseDTO;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "favorites", key = "#loggedInUserEmail")

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
    @CacheEvict(value = "favorites", key = "#loggedInUserEmail")
    public void addFavorite(FavoritesRequestDTO favoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }

        Account account = accountRepository.findByAccountNumber(favoritesRequestDTO.getRecipientAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        if(Boolean.FALSE.equals(account.getAccountName().equals(favoritesRequestDTO.getRecipientAccountName()))){
            throw new ResourceNotFoundException("Recipient account name is incorrect");
        }

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
    @CacheEvict(value = "favorites", key = "#loggedInUserEmail")
    public void deleteFavorite(FavoritesRequestDTO favoritesRequestDTO, String loggedInUserEmail) throws ResourceNotFoundException,UnauthorizedAccessException {
        Customer customer = customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!customer.getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You do not have permission to access this account");
        }



        Account account = accountRepository.findByAccountNumber(favoritesRequestDTO.getRecipientAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));


        if(Boolean.FALSE.equals(account.getAccountName().equals(favoritesRequestDTO.getRecipientAccountName()))){
            throw new ResourceNotFoundException("Recipient account name is incorrect");
        }

        FavRecipientId favRecipientId = new FavRecipientId(customer.getId(), account.getId());

        FavRecipient favRecipient = favoriteRecipientRepository.findById(favRecipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite recipient not found"));

        customer.getFavoriteRecipients().remove(favRecipient);
        favoriteRecipientRepository.deleteById(favRecipientId);
    }

    @Override
    public CustomerResponseDTO getCustomerInfo(String loggedInUserEmail) {
        Customer customer = customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return customer.toResponse();
    }

    @Override
    public CustomerResponseDTO editCustomerInfo(String loggedInUserEmail, EditCustomerDTO editCustomerDTO) {
        Customer customer = customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (editCustomerDTO.getPhoneNumber() != null) {
            customer.setPhoneNumber(editCustomerDTO.getPhoneNumber());
        }
        if (editCustomerDTO.getEmail() != null) {
            customer.setEmail(editCustomerDTO.getEmail());
        }
        if (editCustomerDTO.getFullName() != null) {
            customer.setName(editCustomerDTO.getFullName());
        }
        if (editCustomerDTO.getDateOfBirth() != null) {
            customer.setDateOfBirth(editCustomerDTO.getDateOfBirth());
        }
        if (editCustomerDTO.getCountry() != null) {
            customer.setCountry(editCustomerDTO.getCountry());
        }

        customerRepository.save(customer);

        return customer.toResponse();
    }


}