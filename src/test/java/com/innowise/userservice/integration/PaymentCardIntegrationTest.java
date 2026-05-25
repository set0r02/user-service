package com.innowise.userservice.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PaymentCardIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    private final String BASE_URL = "/api/payment-card";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        jdbcTemplate.execute("TRUNCATE TABLE payment_cards, users RESTART IDENTITY CASCADE;");
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private Object getTestUserId() throws Exception {
        MvcResult userResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Card",
                                    "surname":"Owner",
                                    "birthDate":"1995-05-05",
                                    "email":"owner@test.com",
                                    "active":true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String userResponse = userResult.getResponse().getContentAsString();
        return JsonPath.read(userResponse, "$.id");
    }

    @Test
    void createPaymentCardSuccessfullyTest() throws Exception {
        Object userId = getTestUserId();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "number":"9876987498769876",
                                    "holder":"Petr Petrov",
                                    "expirationDate":"2027-10-05",
                                    "active":true,
                                    "userId": %s
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("9876987498769876"))
                .andExpect(jsonPath("$.holder").value("Petr Petrov"));
    }


    @Test
    void updatePaymentCardTest() throws Exception {
        Object userId = getTestUserId();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "number":"9876987698769876",
                                    "holder":"Petr Petrov",
                                    "expirationDate":"2027-10-05",
                                    "active":true,
                                    "userId": %s
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Object cardId = JsonPath.read(response, "$.id");

        mockMvc.perform(put(BASE_URL + "/" + cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "number":"9999000011112222",
                                    "holder":"New User",
                                    "expirationDate":"2032-12-31",
                                    "active":true,
                                    "userId": %s
                                }
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value("New User"));
    }

    @Test
    void updateCardPaymentStatusTest() throws Exception {
        Object userId = getTestUserId();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "number":"7777666655554444",
                                    "holder":"Status Test",
                                    "expirationDate":"2030-12-31",
                                    "active":true,
                                    "userId": %s
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Object cardId = JsonPath.read(response, "$.id");

        mockMvc.perform(patch(BASE_URL + "/" + cardId + "/status/false"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePaymentCardTest() throws Exception {
        Object userId = getTestUserId();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "number":"9876987698769876",
                                    "holder":"Petr Petrov",
                                    "expirationDate":"2027-10-05",
                                    "active":true,
                                    "userId": %s
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Object cardId = JsonPath.read(response, "$.id");

        mockMvc.perform(delete(BASE_URL + "/" + cardId))
                .andExpect(status().isNoContent());
    }
}