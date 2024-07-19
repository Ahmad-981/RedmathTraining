package com.practice.project06;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.Account;
import com.practice.project06.account.AccountController;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@WebMvcTest(AccountController.class)
public class AccountApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountService accountService;

    private Account account1;
    private Account account2;

    @BeforeEach
    public void setUp() {
        account1 = new Account();
        account1.setAccountID(1L);
        account1.setAccountNumber("123456");
        account1.setAccountType("Checking");
        account1.setPassword("password123");

        account2 = new Account();
        account2.setAccountID(2L);
        account2.setAccountNumber("654321");
        account2.setAccountType("Savings");
        account2.setPassword("password456");
    }

    @Test
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
    public void testGetAccountById() throws Exception {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountID").value(1))
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.accountType").value("Checking"));
    }

//    @Test
//    public void testGetAccountByIdNotFound() throws Exception {
//        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1"))
//                .andExpect(status().isNotFound());
//    }

    @Test
    public void testCreateAccount() throws Exception {
        Mockito.when(accountService.createAccount(any(Account.class))).thenReturn(account1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(account1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountID").value(1))
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.accountType").value("Checking"));
    }

    @Test
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
    public void testUpdateAccountNotFound() throws Exception {
        Mockito.when(accountService.updateAccount(anyLong(), any(Account.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(account1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAccount() throws Exception {
        Mockito.when(accountService.deleteAccount(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAccountNotFound() throws Exception {
        Mockito.when(accountService.deleteAccount(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/1"))
                .andExpect(status().isNotFound());
    }
}

