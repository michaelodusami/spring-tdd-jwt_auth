package com.github.michaelodusami.fakeazon.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.michaelodusami.fakeazon.modules.user.controller.UserAuthController;
import com.github.michaelodusami.fakeazon.modules.user.dto.AuthResponse;
import com.github.michaelodusami.fakeazon.modules.user.dto.LoginRequest;
import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // singals to spring to use the SpringApplication as it's
                                                             // application context
class UserAuthControllerIntegrationTest {

    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAuthController userAuthController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void contextLoads() {
        assertNotNull(userAuthController);
    }

    @Test
    void register_success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("Mike", "mike@gmail.com", "mikepass");
        mockMvc.perform(post("/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isCreated());

    }

    @Test
    void login_success() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("Mike", "mike@gmail.com", "mikepass");
        mockMvc.perform(post("/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        LoginRequest loginRequest = new LoginRequest("mike@gmail.com", "mikepass");
        MvcResult mvcResult = mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(jsonResponse, AuthResponse.class);

        String token = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        assertEquals(registerRequest.getEmail(), authResponse.getEmail());
        assertNotNull(token);
    }

    @Test
    void login_userDoesNotExist() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mike@gmail.com", "mikepass");
        mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
    }

}
