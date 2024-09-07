//package com.transfer.controller;
//
//import com.transfer.dto.request.FavoritesRequestDTO;
//import com.transfer.dto.response.FavoritesResponseDTO;
//import com.transfer.exception.custom.ResourceNotFoundException;
//import com.transfer.service.CustomerService;
//import com.transfer.service.security.JwtUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Collections;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CustomerControllerTest {
//
//    @InjectMocks
//    private CustomerController customerController;
//
//    @Mock
//    private CustomerService customerService;
//
//    @Mock
//    private JwtUtils jwtUtils;
//
//    private static final String TOKEN = "Bearer dummyToken";
//    private static final Long CUSTOMER_ID = 1L;
//    private static final Long SENDER_ID = 1L;
//    private static final Long RECEIVER_ID = 2L;
//    private static final Double AMOUNT = 100.0;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getCustomerById_ShouldReturnCustomerDTO() throws ResourceNotFoundException {
//        CustomerDTO mockCustomerDTO = new CustomerDTO();
//        when(customerService.getCustomerById(CUSTOMER_ID)).thenReturn(mockCustomerDTO);
//
//        CustomerDTO result = customerController.getCustomerById(CUSTOMER_ID).getBody();
//
//        assertNotNull(result);
//        assertEquals(mockCustomerDTO, result);
//        verify(customerService, times(1)).getCustomerById(CUSTOMER_ID);
//    }
//
//    @Test
//    void transfer_ShouldReturnOk() throws ResourceNotFoundException {
//        when(jwtUtils.getEmailFromJwtToken("dummyToken")).thenReturn("user@example.com");
//
//        ResponseEntity<Void> response = customerController.transfer(TOKEN, SENDER_ID, RECEIVER_ID, AMOUNT);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(customerService, times(1)).transfer(SENDER_ID, RECEIVER_ID, AMOUNT, "user@example.com");
//    }
//
//    @Test
//    void getFavorites_ShouldReturnFavorites() throws ResourceNotFoundException {
//        Set<FavoritesResponseDTO> mockFavorites = Collections.emptySet();
//        when(jwtUtils.getEmailFromJwtToken("dummyToken")).thenReturn("user@example.com");
//        when(customerService.getFavorites("user@example.com")).thenReturn(mockFavorites);
//
//        ResponseEntity<Set<FavoritesResponseDTO>> response = customerController.getFavorites(TOKEN);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockFavorites, response.getBody());
//        verify(customerService, times(1)).getFavorites("user@example.com");
//    }
//
//    @Test
//    void addFavorite_ShouldReturnOk() throws ResourceNotFoundException {
//        FavoritesRequestDTO favoritesRequestDTO = new FavoritesRequestDTO();
//        when(jwtUtils.getEmailFromJwtToken("dummyToken")).thenReturn("user@example.com");
//
//        ResponseEntity<Void> response = customerController.addFavorite(TOKEN, favoritesRequestDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(customerService, times(1)).addFavorite(favoritesRequestDTO, "user@example.com");
//    }
//
//    @Test
//    void deleteFavorite_ShouldReturnOk() throws ResourceNotFoundException {
//        FavoritesRequestDTO favoritesRequestDTO = new FavoritesRequestDTO();
//        when(jwtUtils.getEmailFromJwtToken("dummyToken")).thenReturn("user@example.com");
//
//        ResponseEntity<Void> response = customerController.deleteFavorite(TOKEN, favoritesRequestDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(customerService, times(1)).deleteFavorite(favoritesRequestDTO, "user@example.com");
//    }
//
////    @Test
////    void getCustomerById_ShouldThrowResourceNotFoundException() throws ResourceNotFoundException {
////        when(customerService.getCustomerById(CUSTOMER_ID)).thenThrow(new ResourceNotFoundException("Customer not found"));
////
////        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(CUSTOMER_ID);
////
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
////    }
//}