package com.transfer.controller;

import com.transfer.dto.request.RegisterCustomerRequestDTO;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.service.security.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    //testing auth controller
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authenticator;

//    @Test
//    void testRegisterUserWithValidRequestBody() throws Exception {
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\r\n    \"name\": \"Yousef\",\r\n    \"email\": \"r5x@gmail.com\",\r\n      \"accountCurrency\": \"SAR\",\r\n    \"accountType\": \"SAVINGS\"\r\n}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void testRegisterUserWithInvalidRequestBody() throws Exception {
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@gmail.com\" , \"password\":\"testPassword\" , \"userType\":\"SELLER\"}"))
//                .andExpect(status().isBadRequest());
//    }
//
//
//    @Test
//    void testRegisterWithAlreadyExistingUser() throws Exception {
//
//        Mockito.when(this.authenticator.register(any(RegisterCustomerRequestDTO.class)))
//                .thenThrow(new EmailAlreadyExistsException("User already exists"));
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"testUser@gmail.com\", \"password\":\"testPassword\"}"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testAuthenticate() throws Exception {
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"testUser@gmail.com\", \"password\":\"testPassword\"}"))
//                .andExpect(status().isOk());
//    }


}