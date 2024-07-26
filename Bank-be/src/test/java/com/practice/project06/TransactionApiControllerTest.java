package com.practice.project06;

import com.practice.project06.account.Account;
import com.practice.project06.balance.Balance;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.Transaction;
import com.practice.project06.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private BalanceRepository balanceRepository;

    private Account fromAccount;
    private Account toAccount;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        fromAccount = new Account();
        fromAccount.setAccountID(1L);
        fromAccount.setAccountNumber("123456");
        fromAccount.setAccountType("SAVINGS");

        toAccount = new Account();
        toAccount.setAccountID(2L);
        toAccount.setAccountNumber("654321");
        toAccount.setAccountType("CHECKING");

        transaction = new Transaction();
        transaction.setTransactionID(1L);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(BigDecimal.valueOf(1000.0));
        transaction.setDate(new Date());
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.findTransactionsByAccountId(1L)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/by-account")
                        .param("accountId", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionID").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fromAccount.accountID").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].toAccount.accountID").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(1000.0))
                .andDo(print());
    }

//    @Test
//    public void testCreateTransactionSuccess() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
//                        .param("fromAccount", "1")
//                        .param("toAccount", "654321")
//                        .param("amount", "500")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
//                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Transaction successful"))
//                .andDo(print());
//    }

    @Test
    public void testCreateTransactionSuccess() throws Exception {
        // Setup mocks
        Account fromAccount = new Account();
        fromAccount.setAccountID(1L);
        Account toAccount = new Account();
        toAccount.setAccountID(2L);

        Balance fromBalance = new Balance();
        fromBalance.setAmount(BigDecimal.valueOf(2000));
        Balance toBalance = new Balance();
        toBalance.setAmount(BigDecimal.valueOf(1000));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(toAccount));
        when(balanceRepository.findBalanceByAccountId(1L)).thenReturn(fromBalance);
        when(balanceRepository.findBalanceByAccountId(2L)).thenReturn(toBalance);

        // Perform test
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .param("fromAccount", "1")
                        .param("toAccount", "123456")
                        .param("amount", "500")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Transaction successful"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreateTransactionInsufficientBalance() throws Exception {
        // Mock Account objects
        Account fromAccount = new Account();
        fromAccount.setAccountID(1L);
        fromAccount.setAccountNumber("123456"); // Ensure this is consistent with your test

        Account toAccount = new Account();
        toAccount.setAccountID(2L);
        toAccount.setAccountNumber("654321");

        // Mock Balance objects with insufficient balance
        Balance fromBalance = new Balance();
        fromBalance.setAmount(BigDecimal.valueOf(1000)); // Balance is less than the transfer amount

        Balance toBalance = new Balance();
        toBalance.setAmount(BigDecimal.ZERO); // Initialize to avoid null issues

        // Mock repository responses
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("654321")).thenReturn(Optional.of(toAccount));
        when(balanceRepository.findBalanceByAccountId(1L)).thenReturn(fromBalance);
        when(balanceRepository.findBalanceByAccountId(2L)).thenReturn(toBalance);

        // Mock service behavior to throw exception for insufficient balance
        doThrow(new IllegalArgumentException("Low balance")).when(transactionService)
                .createTransaction(1L, "654321", BigDecimal.valueOf(1500));

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .param("fromAccount", "1")
                        .param("toAccount", "654321")
                        .param("amount", "1500") // Transfer amount exceeds balance
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Low balance"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreateTransactionInvalidToAccount() throws Exception {
        // Setup mocks
        Account fromAccount = new Account();
        fromAccount.setAccountID(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("654321")).thenReturn(Optional.empty());

        doThrow(new IllegalArgumentException("Invalid account number")).when(transactionService)
                .createTransaction(1L, "654321", BigDecimal.valueOf(500));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .param("fromAccount", "1")
                        .param("toAccount", "654321")
                        .param("amount", "500")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")
                                .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid to account number"))
                .andDo(MockMvcResultHandlers.print());
    }
}