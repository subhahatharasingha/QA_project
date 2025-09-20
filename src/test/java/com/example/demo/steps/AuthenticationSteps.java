package com.example.demo.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import com.example.demo.bdd.CucumberTest; // Updated import
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
public class AuthenticationSteps extends CucumberTest { // Extended from CucumberTest

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ResponseEntity<String> response;
    private HttpHeaders headers;

    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Given("the application is running")
    public void the_application_is_running() {
        // Application context is loaded by SpringBootTest
        assertNotNull(restTemplate, "TestRestTemplate should be autowired");
        assertNotNull(userRepository, "UserRepository should be autowired");
        assertNotNull(passwordEncoder, "PasswordEncoder should be autowired");
    }

    @Given("I have a clean user database")
    public void i_have_a_clean_user_database() {
        userRepository.deleteAll();
        assertEquals(0, userRepository.count(), "Database should be empty after cleanup");
    }

    @Given("a user already exists with username {string} and email {string}")
    public void a_user_already_exists_with_username_and_email(String username, String email) {
        User user = new User(username, email, passwordEncoder.encode("password123"));
        userRepository.save(user);

        // Verify the user was saved
        assertTrue(userRepository.existsByUsername(username), "User should exist with username: " + username);
        assertTrue(userRepository.existsByEmail(email), "User should exist with email: " + email);
    }

    @Given("a user exists with username {string} and email {string} and password {string}")
    public void a_user_exists_with_username_and_email_and_password(String username, String email, String password) {
        User user = new User(username, email, passwordEncoder.encode(password));
        userRepository.save(user);

        // Verify the user was saved with correct credentials
        User savedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        assertTrue(passwordEncoder.matches(password, savedUser.getPassword()),
                "Password should match for user: " + username);
    }

    @When("I send a POST request to {string} with:")
    public void i_send_a_post_request_to_with(String endpoint, Map<String, String> requestData) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(requestData.get("username"));
        userDTO.setEmail(requestData.get("email"));
        userDTO.setPassword(requestData.get("password"));

        HttpEntity<UserDTO> request = new HttpEntity<>(userDTO, headers);
        response = restTemplate.postForEntity(endpoint, request, String.class);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCodeValue(),
                "Expected status: " + expectedStatus + " but got: " + response.getStatusCodeValue());
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedContent) {
        assertTrue(response.getBody().contains(expectedContent),
                "Response should contain: '" + expectedContent + "' but was: '" + response.getBody() + "'");
    }
}