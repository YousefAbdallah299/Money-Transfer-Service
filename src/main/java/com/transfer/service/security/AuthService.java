package com.transfer.service.security;

import com.transfer.dto.request.ChangePasswordDTO;
import com.transfer.dto.request.LoginRequestDTO;
import com.transfer.dto.response.LoginResponseDTO;
import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.dto.response.CustomerResponseDTO;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.exception.custom.ResourceNotFoundException;


public interface AuthService {

    /**
     * Register a new customer
     *
     * @param customer the customer to be registered
     * @return the registered customer
     * @throws EmailAlreadyExistsException if the customer already exists
     */

    CustomerResponseDTO register(RegisterCustomerRequestDTO customer) throws EmailAlreadyExistsException;


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


    /**
     * Logout a customer
     *
     * @param changePasswordDTO the new password of the user
     */
    void changePassword(ChangePasswordDTO changePasswordDTO, String loggedInUserEmail) throws ResourceNotFoundException;


}
