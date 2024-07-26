package com.practice.project06;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountService accountService;

    private Account account1;
    private Account account2;
    private AccountDTO accountDTO;

    @BeforeEach
    public void setUp() {
        account1 = new Account();
        account1.setAccountID(1L);
        account1.setAccountNumber("123456");
        account1.setAccountType("Checking");

        account2 = new Account();
        account2.setAccountID(2L);
        account2.setAccountNumber("654321");
        account2.setAccountType("Savings");

        accountDTO = new AccountDTO();
        accountDTO.setUser(null); // Mocking User as null for simplicity
        accountDTO.setAccountNumber("123456");
        accountDTO.setAccountType("Checking");
    }

    @Test
    @Order(1)
    public void testGetAllAccounts() throws Exception {
        Mockito.when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountID").value(1))
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[0].accountType").value("Checking"))
                .andExpect(jsonPath("$[1].accountID").value(2))
                .andExpect(jsonPath("$[1].accountNumber").value("654321"))
                .andExpect(jsonPath("$[1].accountType").value("Savings"));
    }

    @Test
    @Order(2)
    public void testGetAccountById() throws Exception {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountID").value(1))
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.accountType").value("Checking"));
    }

    @Test
    @Order(3)
    public void testGetAccountByIdNotFound() throws Exception {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @Order(4)
//    public void testCreateAccount() throws Exception {
//        Mockito.when(accountService.createAccount(any(AccountDTO.class))).thenReturn(account1);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(accountDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountID").value(1))
//                .andExpect(jsonPath("$.accountNumber").value("123456"))
//                .andExpect(jsonPath("$.accountType").value("Checking"));
//    }

    @Test
    @Order(5)
    public void testCreateAccountInvalidInput() throws Exception {
        accountDTO.setAccountNumber(null); // Invalid input

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accountDTO)))
                .andExpect(status().isBadRequest()); // Expecting a Bad Request status
    }

    @Test
    @Order(6)
    public void testUpdateAccount() throws Exception {
        Mockito.when(accountService.updateAccount(anyLong(), any(Account.class))).thenReturn(Optional.of(account1));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(account1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountID").value(1))
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.accountType").value("Checking"));
    }

    @Test
    @Order(7)
    public void testUpdateAccountNotFound() throws Exception {
        Mockito.when(accountService.updateAccount(anyLong(), any(Account.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(account1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    public void testDeleteAccount() throws Exception {
        Mockito.when(accountService.deleteAccount(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(9)
    public void testDeleteAccountNotFound() throws Exception {
        Mockito.when(accountService.deleteAccount(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/1"))
                .andExpect(status().isNotFound());
    }
}
