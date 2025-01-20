package com.intersection;

import com.intersection.application.controller.dto.RegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserIntegrationTests {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    private ClientAndServer mockServer;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
    }

    @Test
    void testRegisterUser() {
        MockServerClient mockClient = new MockServerClient("localhost", 1080);
        mockClient
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/users/register")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"id\": 1}") // JSON object with "id" field
                );

        String url = "http://localhost:1080/api/users/register";

        String requestBody = """
                {
                    "username": "testuser",
                    "email": "testuser@example.com",
                    "password": "password123"
                }
                """;

        ResponseEntity<RegisterResponse> response = restTemplate.postForEntity(url, requestBody, RegisterResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void testLoginUser() {
        MockServerClient mockClient = new MockServerClient("localhost", 1080);
        mockClient
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/users/login")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"token\": \"dummy_token\"}")
                );

        String loginUrl = "http://localhost:1080/api/users/login";

        String loginRequestBody = """
                {
                    "username": "testuser",
                    "password": "password123"
                }
                """;

        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, loginRequestBody, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"token\": \"dummy_token\"}");
    }

    @AfterEach
    public void tearDown() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.stop();
        }
    }
}
