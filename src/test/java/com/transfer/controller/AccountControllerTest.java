//package com.transfer.controller;
//
//import com.transfer.controller.AccountController;
//import com.transfer.dto.CreateAccountDTO;
//import com.transfer.dto.ReturnAccountDTO;
//import com.transfer.dto.ReturnTransactionDTO;
//import com.transfer.dto.UpdateAccountDTO;
//import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
//import com.transfer.exception.custom.ResourceNotFoundException;
//import com.transfer.service.AccountService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AccountControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AccountService accountService;
//
//    @Test
//    void testCreateAccount() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"accountNumber\":\"1234567890\",\"accountCurrency\":\"SAR\",\"accountType\":\"SAVINGS\"}"))
//                .andExpect(status().isCreated());
//    }
//    @Test
//    void testGetAccountByID() throws Exception {
//        Long accountid = 1L;
//        ReturnAccountDTO returnAccountDTO = new ReturnAccountDTO();
//
//        Mockito.when(accountService.getAccountById(accountid)).thenReturn(returnAccountDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/{id}", accountid)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testUpdateAccount() throws Exception {
//        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
//        ReturnAccountDTO returnAccountDTO = new ReturnAccountDTO();
//
//        Mockito.when(accountService.updateAccount(any(UpdateAccountDTO.class))).thenReturn(returnAccountDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"accountNumber\":\"1234567890\",\"accountCurrency\":\"SAR\",\"accountType\":\"SAVINGS\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteAccount() throws Exception {
//        Long accountId = 1L;
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/account/delete/{id}", accountId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeposit() throws Exception {
//        Long accountId = 1L;
//        Double amount = 10.0;
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account/deposit/{id}/{amount}", accountId, amount)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetBalance() throws Exception {
//        Long accountID = 1L;
//        Double balance = 10.0;
//        Mockito.when(accountService.getBalance(accountID)).thenReturn(balance);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/balance/{accountid}", accountID)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//    @Test
//    void testGetTransactions() throws Exception {
//        Long accountID = 1L;
//        Set<ReturnTransactionDTO> transactions = Set.of(new ReturnTransactionDTO());
//        Mockito.when(accountService.getTransactions(accountID)).thenReturn(transactions);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/{accountid}/transactions", accountID)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//}