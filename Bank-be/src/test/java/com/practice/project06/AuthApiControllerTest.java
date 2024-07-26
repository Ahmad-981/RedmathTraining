package com.practice.project06;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @Test
    public void testLogin_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"admin\", \"password\":\"admin123\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.notNullValue())) // Assuming token is returned
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.notNullValue())); // Assuming userId is returned
    }

    @Order(2)
    @Test
    public void testLogin_Unauthorised() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"abcd\", \"password\":\"sdsd\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Order(3)
    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"abcd\", \"password\":\"abc\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Order(4)
    @Test
    public void testRegister_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"newUser\", \"password\":\"newPassword\", \"role\":\"USER\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("newUser")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", Matchers.is("USER")));
    }
}