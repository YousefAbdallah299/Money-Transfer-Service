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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FavoriteRecipientRepository favoriteRecipientRepository;

    @Mock
    private CacheManager cacheManager;

    private Cache cache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cache = mock(Cache.class);
        when(cacheManager.getCache(any(String.class))).thenReturn(cache);
    }

    @Test
    void getAccounts_ShouldReturnAccountDTOs() throws ResourceNotFoundException, UnauthorizedAccessException {
        String email = "test@example.com";

        Customer customer = new Customer();
        customer.setEmail(email);
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setAccountName("Test Account");
        customer.setAccounts(Collections.singleton(account));

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        List<AccountResponseDTO> response = customerService.getAccounts(email);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("12345", response.get(0).getAccountNumber());
    }

    @Test
    void getAccounts_ShouldThrowUnauthorizedAccessException() {
        String email = "test@example.com";

        Customer customer = new Customer();
        customer.setEmail("other@example.com");

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            customerService.getAccounts(email);
        });

        assertEquals("You do not have permission to access this account", exception.getMessage());
    }

    @Test
    void getFavorites_ShouldReturnFavoriteDTOs() throws ResourceNotFoundException, UnauthorizedAccessException {
        String email = "test@example.com";

        Customer customer = new Customer();
        customer.setEmail(email);
        FavRecipient favRecipient = new FavRecipient();
        favRecipient.setRecipientAccount(new Account());
        favRecipient.setRecipientName("Test Recipient");
        customer.setFavoriteRecipients(Collections.singleton(favRecipient));

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        Set<FavoritesResponseDTO> response = customerService.getFavorites(email);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Recipient", response.iterator().next().getRecipientName());
    }

    @Test
    void getFavorites_ShouldThrowUnauthorizedAccessException() {
        String email = "test@example.com";

        Customer customer = new Customer();
        customer.setEmail("other@example.com");

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            customerService.getFavorites(email);
        });

        assertEquals("You do not have permission to access this account", exception.getMessage());
    }

    @Test
    void addFavorite_ShouldAddFavorite() throws ResourceNotFoundException, UnauthorizedAccessException {
        String email = "test@example.com";
        FavoritesRequestDTO requestDTO = new FavoritesRequestDTO();
        requestDTO.setRecipientAccountNumber("12345");
        requestDTO.setRecipientAccountName("Test Account");

        Customer customer = new Customer();
        customer.setEmail(email);
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setAccountName("Test Account");

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        customerService.addFavorite(requestDTO, email);

        verify(favoriteRecipientRepository).save(any(FavRecipient.class));
    }

    @Test
    void addFavorite_ShouldThrowUnauthorizedAccessException() {
        String email = "test@example.com";
        FavoritesRequestDTO requestDTO = new FavoritesRequestDTO();
        requestDTO.setRecipientAccountNumber("12345");
        requestDTO.setRecipientAccountName("Test Account");

        Customer customer = new Customer();
        customer.setEmail("other@example.com");

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            customerService.addFavorite(requestDTO, email);
        });

        assertEquals("You do not have permission to access this account", exception.getMessage());
    }

    @Test
    void deleteFavorite_ShouldDeleteFavorite() throws ResourceNotFoundException, UnauthorizedAccessException {
        String email = "test@example.com";
        FavoritesRequestDTO requestDTO = new FavoritesRequestDTO();
        requestDTO.setRecipientAccountNumber("12345");
        requestDTO.setRecipientAccountName("Test Account");

        Customer customer = new Customer();
        customer.setEmail(email);
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setAccountName("Test Account");

        FavRecipientId favRecipientId = new FavRecipientId(customer.getId(), account.getId());
        FavRecipient favRecipient = new FavRecipient();
        favRecipient.setId(favRecipientId);

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(favoriteRecipientRepository.findById(favRecipientId)).thenReturn(Optional.of(favRecipient));

        customerService.deleteFavorite(requestDTO, email);

        verify(favoriteRecipientRepository).deleteById(favRecipientId);
    }

    @Test
    void deleteFavorite_ShouldThrowUnauthorizedAccessException() {
        String email = "test@example.com";
        FavoritesRequestDTO requestDTO = new FavoritesRequestDTO();
        requestDTO.setRecipientAccountNumber("12345");
        requestDTO.setRecipientAccountName("Test Account");

        Customer customer = new Customer();
        customer.setEmail("other@example.com");

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            customerService.deleteFavorite(requestDTO, email);
        });

        assertEquals("You do not have permission to access this account", exception.getMessage());
    }

    @Test
    void getCustomerInfo_ShouldReturnCustomerResponseDTO() {
        String email = "test@example.com";

        Customer customer = new Customer();
        customer.setEmail(email);

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));

        CustomerResponseDTO response = customerService.getCustomerInfo(email);

        assertNotNull(response);
        assertEquals(email, response.getEmail());
    }


    @Test
    void editCustomerInfo_ShouldUpdateCustomerInfo() {
        String email = "test@example.com";
        EditCustomerDTO editDTO = new EditCustomerDTO();
        editDTO.setPhoneNumber("123456789");
        editDTO.setFullName("Updated Name");

        Customer customer = new Customer();
        customer.setEmail(email);

        when(customerRepository.findUserByEmail(email)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = customerService.editCustomerInfo(email, editDTO);

        assertNotNull(response);
        assertEquals("123456789", customer.getPhoneNumber());
        assertEquals("Updated Name", customer.getName());
    }

}
