package com.transfer.controller;

import com.transfer.dto.request.EditCustomerDTO;
import com.transfer.dto.request.FavoritesRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.CustomerResponseDTO;
import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.CustomerService;
import com.transfer.service.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private JwtUtils jwtUtils;

    private String token;
    private String email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        token = "Bearer someJwtToken";
        email = "user@example.com";
    }

    @Test
    void getCustomerInfo_ShouldReturnCustomerInfo() throws ResourceNotFoundException {
        CustomerResponseDTO response = new CustomerResponseDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(customerService.getCustomerInfo(email)).thenReturn(response);

        ResponseEntity<CustomerResponseDTO> result = customerController.getCustomerInfo(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(customerService).getCustomerInfo(email);
    }

    @Test
    void getCustomerInfo_ShouldReturnNotFoundStatus() throws ResourceNotFoundException {
        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        doThrow(new ResourceNotFoundException("Customer not found")).when(customerService).getCustomerInfo(email);

        ResponseEntity<CustomerResponseDTO> result = customerController.getCustomerInfo(token);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        verify(customerService).getCustomerInfo(email);
    }

    @Test
    void editCustomerInfo_ShouldReturnUpdatedCustomerInfo() throws ResourceNotFoundException {
        EditCustomerDTO editCustomerDTO = new EditCustomerDTO();
        CustomerResponseDTO response = new CustomerResponseDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(customerService.editCustomerInfo(email, editCustomerDTO)).thenReturn(response);

        ResponseEntity<CustomerResponseDTO> result = customerController.editCustomerInfo(token, editCustomerDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(customerService).editCustomerInfo(email, editCustomerDTO);
    }





    @Test
    void getAccounts_ShouldReturnListOfAccounts() throws ResourceNotFoundException {
        List<AccountResponseDTO> accounts = new ArrayList<>();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(customerService.getAccounts(email)).thenReturn(accounts);

        ResponseEntity<List<AccountResponseDTO>> result = customerController.getAccounts(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(accounts, result.getBody());
        verify(customerService).getAccounts(email);
    }


    @Test
    void getFavorites_ShouldReturnSetOfFavorites() throws ResourceNotFoundException {
        Set<FavoritesResponseDTO> favorites = new HashSet<>();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);
        when(customerService.getFavorites(email)).thenReturn(favorites);

        ResponseEntity<Set<FavoritesResponseDTO>> result = customerController.getFavorites(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(favorites, result.getBody());
        verify(customerService).getFavorites(email);
    }



    @Test
    void addFavorite_ShouldReturnOk() throws ResourceNotFoundException {
        FavoritesRequestDTO favoritesRequestDTO = new FavoritesRequestDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);

        ResponseEntity<Void> result = customerController.addFavorite(token, favoritesRequestDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
        verify(customerService).addFavorite(favoritesRequestDTO, email);
    }





    @Test
    void deleteFavorite_ShouldReturnOk() throws ResourceNotFoundException {
        FavoritesRequestDTO favoritesRequestDTO = new FavoritesRequestDTO();

        when(jwtUtils.getEmailFromJwtToken(any(String.class))).thenReturn(email);

        ResponseEntity<Void> result = customerController.deleteFavorite(token, favoritesRequestDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
        verify(customerService).deleteFavorite(favoritesRequestDTO, email);
    }

}
