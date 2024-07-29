package com.practice.project06;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.Balance;
import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.Transaction;
import com.practice.project06.transaction.TransactionController;
import com.practice.project06.transaction.TransactionDTO;
import com.practice.project06.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;

import java.util.Optional;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private TransactionController transactionController;

    private String jwtToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authenticate();
    }

    private void authenticate() throws Exception {
        var loginResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"admin\", \"password\":\"admin123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        // Extract the JWT token from the JSON response
        String responseBody = loginResponse.getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        jwtToken = jsonResponse.get("token").asText();
    }

    @Order(1)
    @Test
    public void testGetTransactionsByAccountId_Success() throws Exception {
        Long accountId = 1L;
        Account fromAccount = new Account();
        fromAccount.setAccountID(accountId);

        Account toAccount = new Account();
        toAccount.setAccountID(2L);

        Transaction transaction1 = new Transaction(fromAccount, toAccount, BigDecimal.valueOf(1000.00));
        Transaction transaction2 = new Transaction(fromAccount, toAccount, BigDecimal.valueOf(500.00));

        when(transactionService.findTransactionsByAccountId(accountId)).thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/by-account")
                        .param("id", accountId.toString())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }


    @Test
    public void testCreateTransaction_Success() throws Exception {
        Long fromAccountID = 1L;
        String toAccountNumber = "12345";
        BigDecimal amount = BigDecimal.valueOf(1000.00);

        Account fromAccount = new Account();
        fromAccount.setAccountID(fromAccountID);

        Account toAccount = new Account();
        toAccount.setAccountID(2L);

        Balance fromBalance = new Balance();
        fromBalance.setAmount(BigDecimal.valueOf(2000.00));

        Balance toBalance = new Balance();
        toBalance.setAmount(BigDecimal.valueOf(1000.00));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        Transaction transaction = new Transaction(fromAccount, toAccount, amount);

        // Mock repository responses
        when(accountRepository.findById(fromAccountID)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(balanceRepository.findBalanceByAccountId(fromAccountID)).thenReturn(fromBalance);
        when(balanceRepository.findBalanceByAccountId(toAccount.getAccountID())).thenReturn(toBalance);
        when(transactionService.createTransaction(fromAccountID, toAccountNumber, amount)).thenReturn(transaction);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount.intValue()));
    }

    @Test
    public void testCreateTransaction_InvalidFromAccount() throws Exception {
        Long fromAccountID = 3L;
        String toAccountNumber = "ACC123";
        BigDecimal amount = BigDecimal.valueOf(1000.00);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        when(accountRepository.findById(fromAccountID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                //.andExpect(MockMvcResultMatchers.status().isBadRequest());
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid from account ID"));
    }
//
//    @Order(4)
//    @Test
//    public void testCreateTransaction_InvalidToAccount() throws Exception {
//        Long fromAccountID = 1L;
//        String toAccountNumber = "Adsda"; // Invalid account number
//        BigDecimal amount = BigDecimal.valueOf(1000.00);
//
//        // Set up the account details
//        Account fromAccount = new Account();
//        fromAccount.setAccountID(fromAccountID);
//
//        // Create the DTO object for the request
//        TransactionDTO transactionDTO = new TransactionDTO();
//        transactionDTO.setFromAccountID(fromAccountID);
//        transactionDTO.setToAccountNumber(toAccountNumber);
//        transactionDTO.setAmount(amount);
//        transactionDTO.setIndicator("credit");
//
//        // Mock repository responses
//        when(accountRepository.findById(fromAccountID)).thenReturn(Optional.of(fromAccount));
//        when(accountRepository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.empty()); // Simulate invalid to account
//
//        // Perform the request
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .content(objectMapper.writeValueAsString(transactionDTO)))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid to account number")) // Adjusted expected message
//                .andDo(MockMvcResultHandlers.print());
//    }
//

    @Test
    public void testCreateTransaction_InsufficientBalance() throws Exception {
        Long fromAccountID = 1L;
        String toAccountNumber = "ACC123";
        BigDecimal amount = BigDecimal.valueOf(10000.00);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        Account fromAccount = new Account();
        fromAccount.setAccountID(fromAccountID);
        fromAccount.setAccountNumber("123456");

        Account toAccount = new Account();
        toAccount.setAccountID(2L);
        toAccount.setAccountNumber(toAccountNumber);

        Balance fromBalance = new Balance();
        fromBalance.setAmount(BigDecimal.valueOf(5000.00)); // Insufficient balance

        Balance toBalance = new Balance();
        toBalance.setAmount(BigDecimal.ZERO);

        when(accountRepository.findById(fromAccountID)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(balanceRepository.findBalanceByAccountId(fromAccountID)).thenReturn(fromBalance);
        when(balanceRepository.findBalanceByAccountId(toAccount.getAccountID())).thenReturn(toBalance);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Low balance"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Order(6)
    @Test
    public void testCreateTransaction_InvalidToken() throws Exception {
        Long fromAccountID = 1L;
        String toAccountNumber = "ACC123";
        BigDecimal amount = BigDecimal.valueOf(1000.00);

        Account fromAccount = new Account();
        fromAccount.setAccountID(fromAccountID);

        Account toAccount = new Account();
        toAccount.setAccountID(2L);

        Balance fromBalance = new Balance();
        fromBalance.setAmount(BigDecimal.valueOf(2000.00));

        Balance toBalance = new Balance();
        toBalance.setAmount(BigDecimal.valueOf(1000.00));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        when(accountRepository.findById(fromAccountID)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(balanceRepository.findBalanceByAccountId(fromAccountID)).thenReturn(fromBalance);
        when(balanceRepository.findBalanceByAccountId(toAccount.getAccountID())).thenReturn(toBalance);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
