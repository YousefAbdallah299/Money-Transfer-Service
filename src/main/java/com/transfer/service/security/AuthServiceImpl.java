package com.transfer.service.security;

import com.transfer.dto.LoginRequestDTO;
import com.transfer.dto.LoginResponseDTO;
import com.transfer.dto.RegisterCustomerRequest;
import com.transfer.dto.RegisterCustomerResponse;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterCustomerResponse register(RegisterCustomerRequest customerRequest) throws EmailAlreadyExistsException{

        if(Boolean.TRUE.equals(customerRepository.existsByEmail(customerRequest.getEmail())))
            throw new EmailAlreadyExistsException("Email Already Exists!");

        Customer customer = Customer.builder()
                .email(customerRequest.getEmail())
                .password(this.passwordEncoder.encode(customerRequest.getPassword()))
                .name(customerRequest.getName())
                .build();

        Account account = Account.builder()
                .balance(0.0)
                .accountType(customerRequest.getAccountType())
                .accountDescription("Savings Account")
                .accountName(customerRequest.getName())
                .currency(customerRequest.getAccountCurrency())
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .customer(customer)
                .build();

        customer.getAccounts().add(account);

        Customer savedCustomer = customerRepository.save(customer);

        return savedCustomer.toResponse();
    }

    
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Success!")
                .httpStatus(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .build();

    }

}
