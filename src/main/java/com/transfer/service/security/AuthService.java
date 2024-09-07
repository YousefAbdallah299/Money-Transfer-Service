package com.transfer.service.security;

import com.transfer.dto.request.LoginRequestDTO;
import com.transfer.dto.response.LoginResponseDTO;
import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.dto.response.RegisterCustomerResponseDTO;
import com.transfer.exception.custom.EmailAlreadyExistsException;


public interface AuthService {

    /**
     * Register a new customer
     *
     * @param customer the customer to be registered
     * @return the registered customer
     * @throws EmailAlreadyExistsException if the customer already exists
     */

    RegisterCustomerResponseDTO register(RegisterCustomerRequestDTO customer) throws EmailAlreadyExistsException;


    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);


    /**
     * Logout a customer
     *
     * @param token the token of the user
     */
    void logout(String token);

}
