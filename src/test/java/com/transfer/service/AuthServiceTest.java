package com.transfer.service;

import com.transfer.dto.request.ChangePasswordDTO;
import com.transfer.dto.request.LoginRequestDTO;
import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.dto.response.CustomerResponseDTO;
import com.transfer.dto.response.LoginResponseDTO;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.exception.custom.InvalidOldPasswordException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.SameAsOldPasswordException;
import com.transfer.repository.CustomerRepository;
import com.transfer.service.security.AuthServiceImpl;
import com.transfer.service.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    private Set<String> invalidatedTokens;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invalidatedTokens = new HashSet<>();
        authService = new AuthServiceImpl(customerRepository, passwordEncoder, jwtUtils, authenticationManager);
    }

    @Test
    void register_ShouldCreateCustomer() throws EmailAlreadyExistsException {
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("John Doe");

        Customer customer = new Customer();
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setName(request.getName());

        when(customerRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = authService.register(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void register_ShouldThrowEmailAlreadyExistsException() throws EmailAlreadyExistsException {
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        request.setEmail("test@example.com");

        when(customerRepository.existsByEmail(any(String.class))).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.register(request);
        });

        assertEquals("Email Already Exists!", exception.getMessage());
    }

    @Test
    void login_ShouldReturnJwtToken() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwtToken");

        LoginResponseDTO response = authService.login(request);

        assertEquals("jwtToken", response.getToken());
        assertEquals(HttpStatus.ACCEPTED, response.getHttpStatus());
        assertEquals("Bearer", response.getTokenType());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_ShouldThrowExceptionForInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void logout_ShouldInvalidateToken() {
        String token = "Bearer someJwtToken";

        authService.logout(token);

        assertTrue(authService.isTokenInvalid(token));
    }



    @Test
    void changePassword_ShouldThrowInvalidOldPasswordException() throws ResourceNotFoundException, SameAsOldPasswordException, InvalidOldPasswordException {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("wrongOldPassword");
        changePasswordDTO.setNewPassword("newPassword");

        Customer customer = new Customer();
        customer.setPassword(passwordEncoder.encode("correctOldPassword"));

        when(customerRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(eq("wrongOldPassword"), any(String.class))).thenReturn(false);

        InvalidOldPasswordException exception = assertThrows(InvalidOldPasswordException.class, () -> {
            authService.changePassword(changePasswordDTO, "test@example.com");
        });

        assertEquals("Old password is incorrect!", exception.getMessage());
    }
}

