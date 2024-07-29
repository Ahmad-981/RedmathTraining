package com.practice.project06;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.*;
import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.user.User;
import com.practice.project06.user.UserRepository;
import com.practice.project06.user.UserService;
import com.practice.project06.user.UserController;
import com.practice.project06.user.UserService;
import com.practice.project06.transaction.TransactionRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private String jwtToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authenticate();
        User user = new User();
        user.setUsername("user");
        user.setUserID(1L); // Set a valid user ID

        // Create an Account object with the User included
        Account account = new Account();
        account.setAccountID(1L);
        account.setUser(user);

        // Mock the repository behavior
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
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

    @Test
    public void testGetAllAccounts() throws Exception {
        Account account1 = new Account();
        account1.setAccountID(1L);
        account1.setAccountNumber("12345");
        account1.setAccountType("Savings");

        Account account2 = new Account();
        account2.setAccountID(2L);
        account2.setAccountNumber("123456");
        account2.setAccountType("Checking");

        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountID").value(2));
    }


    @Order(1)
    @Test
    public void testGetAccountById_Success() throws Exception {

        Account account = new Account();
        account.setAccountID(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    public void testCreateAccount() throws Exception {
        User user = new User();
        user.setUserID(4L);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("12345678");
        accountDTO.setAccountType("Savings");
        accountDTO.setUser(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateAccount() throws Exception {
        // Create an Account object with updated values
        Account account = new Account();
        account.setAccountID(1L);
        account.setAccountNumber("1234567");
        account.setAccountType("Savings");

        when(accountService.updateAccount(eq(1L), account)).thenReturn(Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(account)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("1234567"));
    }

    @Test
    public void testDeleteAccount_Success() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setUserID(3L);

        Account account = new Account();
        account.setAccountID(3L);
        account.setUser(user);

        when(accountRepository.findById(3L)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).deleteById(3L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/3")
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testFindByAccountNumber() {
        Account account = new Account();
        account.setAccountNumber("12345678");

        when(accountRepository.findByAccountNumber("12345678")).thenReturn(Optional.of(account));

        Optional<Account> result = accountRepository.findByAccountNumber("12345678");
        assertTrue(result.isPresent());
        assertEquals("12345678", result.get().getAccountNumber());
    }

    @Test
    public void testFindByUser_UserID() {
        User user = new User();
        user.setUserID(1L);

        Account account = new Account();
        account.setUser(user);

        when(accountRepository.findByUser_UserID(1L)).thenReturn(Optional.of(account));

        Optional<Account> result = accountRepository.findByUser_UserID(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get().getUser());
    }


    @Test
    public void testFindByUser_Username() {
        User user = new User();
        user.setUsername("user");

        Account account = new Account();
        account.setUser(user);

        when(accountRepository.findByUser_Username("user")).thenReturn(Optional.of(account));

        Optional<Account> result = accountRepository.findByUser_Username("user");
        assertTrue(result.isPresent());
        assertEquals("user", result.get().getUser().getUsername());
    }
}
