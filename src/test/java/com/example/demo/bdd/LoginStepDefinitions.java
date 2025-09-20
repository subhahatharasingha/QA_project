//package com.example.demo.bdd;
//
//import com.example.demo.dto.UserDTO;
//import com.example.demo.model.User;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.service.UserService;
//import org.mockito.Mockito;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import io.cucumber.java.en.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Optional;
//
//public class LoginStepDefinitions {
//
//    private UserService userService;
//    private UserRepository userRepository;
//    private BCryptPasswordEncoder passwordEncoder;
//    private String loginResult;
//
//    @Given("a user with email {string} and password {string}")
//    public void a_user_with_email_and_password(String email, String password) {
//        userRepository = Mockito.mock(UserRepository.class);
//        passwordEncoder = new BCryptPasswordEncoder();
//        userService =  new UserService(userRepository, passwordEncoder);
//
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//
//        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//    }
//
//    @When("the user tries to login with email {string} and password {string}")
//    public void the_user_tries_to_login(String email, String password) {
//        UserDTO dto = new UserDTO();
//        dto.setEmail(email);
//        dto.setPassword(password);
//        loginResult = userService.login(dto);
//    }
//
//    @Then("the login should be successful")
//    public void the_login_should_be_successful() {
//        assertEquals("Login successful!", loginResult);
//    }
//
//    @Then("the login should fail")
//    public void the_login_should_fail() {
//        assertEquals("Invalid email or password!", loginResult);
//    }
//}
