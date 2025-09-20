package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister_WithUniqueUsername_UserCreatedSuccessfully() {
        // Arrange - Setup test data
        UserDTO dto = new UserDTO("newuser", "new@example.com", "password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String result = userService.register(dto);

        assertEquals("User registered successfully!", result);
        verify(userRepository).save(any(User.class)); // Verify user was saved
    }

    @Test
    void testRegister_WithExistingUsername_ThrowsUsernameAlreadyExists() {
        // Arrange - Setup test data with existing username
        UserDTO dto = new UserDTO("existinguser", "new@example.com", "password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.register(dto));

        assertEquals("Username already exists!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Verify user was NOT saved
    }
}