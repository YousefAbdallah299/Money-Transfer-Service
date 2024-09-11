package com.transfer.service.security;

import com.transfer.dto.enums.AccountCurrency;
import com.transfer.dto.enums.AccountType;
import com.transfer.dto.request.ChangePasswordDTO;
import com.transfer.dto.request.LoginRequestDTO;
import com.transfer.dto.response.LoginResponseDTO;
import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.dto.response.CustomerResponseDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.exception.custom.InvalidOldPasswordException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.custom.SameAsOldPasswordException;
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
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final Set<String> invalidatedTokens = new HashSet<>();

    @Transactional
    public CustomerResponseDTO register(RegisterCustomerRequestDTO customerRequest) throws EmailAlreadyExistsException{

        if(Boolean.TRUE.equals(customerRepository.existsByEmail(customerRequest.getEmail())))
            throw new EmailAlreadyExistsException("Email Already Exists!");

        Customer customer = Customer.builder()
                .email(customerRequest.getEmail())
                .password(this.passwordEncoder.encode(customerRequest.getPassword()))
                .name(customerRequest.getName())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .country(customerRequest.getCountry())
                .build();

        if(customerRequest.getPhoneNumber() != null) customer.setPhoneNumber(customerRequest.getPhoneNumber());

        Account account = Account.builder()
                .balance(0.0)
                .accountType(AccountType.SAVINGS)
                .accountDescription("Savings Account")
                .accountName(customerRequest.getName())
                .currency(AccountCurrency.EGP)
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

    @Override
    public void logout(String token){
        invalidatedTokens.add(token);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, String loggedInUserEmail) throws ResourceNotFoundException, SameAsOldPasswordException, InvalidOldPasswordException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newPass = passwordEncoder.encode(changePasswordDTO.getNewPassword());

        if(Boolean.TRUE.equals(newPass.equals(passwordEncoder.encode(changePasswordDTO.getOldPassword()))))
            throw new SameAsOldPasswordException("New password is the same as the old one!");

        if(Boolean.FALSE.equals(customer.getPassword().equals(passwordEncoder.encode(changePasswordDTO.getOldPassword())))){
            throw new InvalidOldPasswordException("Old password is incorrect!");
        }

        customer.setPassword(newPass);


        customerRepository.save(customer);


    }

    public boolean isTokenInvalid(String token){
        return invalidatedTokens.contains(token);
    }

}
