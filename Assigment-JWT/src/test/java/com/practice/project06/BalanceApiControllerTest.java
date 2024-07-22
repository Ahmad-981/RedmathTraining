package com.practice.project06;

import com.practice.project06.account.Account;
import com.practice.project06.balance.Balance;
import com.practice.project06.balance.BalanceController;
import com.practice.project06.balance.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@WebMvcTest(BalanceController.class)
public class BalanceApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceRepository balanceRepository;

    private Balance balance;
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setAccountID(1L);
        account.setAccountNumber("123456");
        account.setAccountType("Checking");
        account.setPassword("password123");

        balance = new Balance();
        balance.setBalanceID(1L);
        balance.setAccount(account);
        balance.setDate(new Date());
        balance.setAmount(new BigDecimal("1000.0"));
        balance.setIndicator("positive");
    }

    @Test
    public void testGetBalanceById() throws Exception {
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account.accountNumber").value("123456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("positive"))
                .andDo(print());
    }

    @Test
    public void testGetBalanceByIdNotFound() throws Exception {
        when(balanceRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/balances/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }
}
