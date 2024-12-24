package com.github.michaelodusami.fakeazon.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
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
import org.springframework.security.web.FilterChainProxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.michaelodusami.fakeazon.modules.user.dto.LoginRequest;
import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

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
        private FilterChainProxy filterChainProxy;

        @Autowired
        private UserRepository userRepository;

        private User admin;
        private User user;

        private String adminToken;
        private String userToken;

        @BeforeEach
        void setUp(TestInfo testInfo) throws Exception {

                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true)
                                .addFilters(filterChainProxy).build();
                admin = User.builder().name("Admin").password("admimnpassword").email("admin@admin.com")
                                .roles(Set.of("ROLE_ADMIN")).createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now()).build();

                user = User.builder().name("User").password("userpass").email("user@user.com")
                                .roles(Set.of("ROLE_USER"))
                                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

                // register both users
                mockMvc.perform(post("/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(RegisterRequest.toRegisterRequest(user))));

                mockMvc.perform(post("/v1/auth/register/admin").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(RegisterRequest.toRegisterRequest(admin))));

                // login and get tokens
                MvcResult mvcResult = mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(LoginRequest.toLoginRequest(user))))
                                .andReturn();

                userToken = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

                mvcResult = mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(LoginRequest.toLoginRequest(admin))))
                                .andReturn();

                adminToken = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

                // get users
                user = userRepository.findByEmail(user.getEmail()).get();
                admin = userRepository.findByEmail(admin.getEmail()).get();

        }

        @Test
        void contextLoads() {
                assertNotNull(user);
                assertNotNull(admin);
                assertNotNull(userToken);
                assertNotNull(adminToken);
                assertNotEquals(userToken, adminToken);
        }

        @Test
        void accessControllerWithNoToken() throws Exception {

                mockMvc.perform(get("/v1/users")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ""))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getUserById_success() throws Exception {
                MvcResult result = mockMvc.perform(get("/v1/users/{id}", user.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                                .andExpect(status().isOk())
                                .andReturn();

                User fetchedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
                System.out.println(fetchedUser);
                assertEquals(user.getEmail(), fetchedUser.getEmail());
        }

        @Test
        void getUserById_doesNotExist() throws Exception {
                mockMvc.perform(get("/v1/users/{id}", 3)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                                .andExpect(status().isNotFound())
                                .andReturn();
        }

        @Test
        void getUserByEmail_Success() throws Exception {
                mockMvc.perform(get("/v1/users/email/{email}", user.getEmail())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void updateUser_notFound() throws Exception {
                User updatedUser = User.builder()
                                .name("Updated User")
                                .email("doesnotexist@user.com")
                                .build();

                mockMvc.perform(put("/v1/users/{id}", 999)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                                .content(objectMapper.writeValueAsString(updatedUser)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deleteUserById_success() throws Exception {
                mockMvc.perform(delete("/v1/users/{id}", user.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                                .andExpect(status().isOk());
        }

        @Test
        void deleteUserById_notFound() throws Exception {
                mockMvc.perform(delete("/v1/users/{id}", 999)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                                .andExpect(status().isNotFound());
        }

        @Test
        void changePassword_success() throws Exception {
                String newPassword = "newpassword123";

                mockMvc.perform(patch("/v1/users/{id}/password", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                                .content(newPassword))
                                .andExpect(status().isOk());
        }

        @Test
        void changePassword_notFound() throws Exception {
                String newPassword = "newpassword123";

                mockMvc.perform(patch("/v1/users/{id}/password", 999)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                                .content(newPassword))
                                .andExpect(status().isNotFound());
        }

}
