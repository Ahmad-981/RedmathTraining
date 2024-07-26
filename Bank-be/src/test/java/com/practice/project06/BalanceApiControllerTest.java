package com.practice.project06;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.*;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BalanceApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceService balanceService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BalanceController balanceController;

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

        String responseBody = loginResponse.getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        jwtToken = jsonResponse.get("token").asText();
    }

    @Test
    public void testCreateBalance_Success() throws Exception {
        Account account = new Account();
        account.setAccountID(3L);
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setAccountID(1L);
        balanceDTO.setAmount(BigDecimal.valueOf(1000.00));
        balanceDTO.setIndicator("credit");

        Balance balance = new Balance();
        balance.setBalanceID(1L);
        balance.setAccount(account);
        balance.setAmount(BigDecimal.valueOf(1000.00));
        balance.setIndicator("credit");
        balance.setDate(new Date());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(balanceRepository.findByAccount(account)).thenReturn(Optional.empty());
        when(balanceService.createBalance(any(Balance.class))).thenReturn(balance);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/balance")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(balanceDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("credit"));
    }

    @Test
    public void testCreateBalance_BalanceAlreadyExists() throws Exception {
        Account account = new Account();
        account.setAccountID(1L);
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setAccountID(1L);
        balanceDTO.setAmount(BigDecimal.valueOf(1000.00));
        balanceDTO.setIndicator("credit");

        Balance existingBalance = new Balance();
        existingBalance.setBalanceID(1L);
        existingBalance.setAccount(account);
        existingBalance.setAmount(BigDecimal.valueOf(1000.00));
        existingBalance.setIndicator("credit");
        existingBalance.setDate(new Date());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(balanceRepository.findByAccount(account)).thenReturn(Optional.of(existingBalance));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/balance")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(balanceDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("Balance already exists for this account"));
    }
    @Test
    public void testGetBalanceByAccountId_Success() throws Exception {
        // Arrange
        Long accountId = 3L;

        // Create a mock Account object
        Account account = new Account();
        account.setAccountID(accountId);

        // Create a mock Balance object
        Balance balance = new Balance();
        balance.setBalanceID(1L);
        balance.setAccount(account);
        balance.setAmount(BigDecimal.valueOf(1000.00));
        balance.setIndicator("credit");
        balance.setDate(new Date());

        // Mock the repository methods
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(balanceRepository.findByAccount(account)).thenReturn(Optional.of(balance));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/" + accountId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
               // .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("credit"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account.accountID").value(accountId));
    }



    @Test
    public void testGetBalanceById_Success() throws Exception {
        // Arrange
        Account account = new Account();
        account.setAccountID(1L);
        Balance balance = new Balance();
        balance.setBalanceID(1L);
        balance.setAccount(account);
        balance.setAmount(BigDecimal.valueOf(1000.00));
        balance.setIndicator("credit");
        balance.setDate(new Date());

        when(balanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("credit"));
    }

}
