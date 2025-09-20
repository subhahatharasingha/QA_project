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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister_WhenUsernameExists_ShouldThrowException() {
        // Arrange
        UserDTO dto = new UserDTO("existingUser", "test@example.com", "password");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.register(dto));

        assertEquals("Username already exists!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_WhenInvalidCredentials_ShouldReturnError() {
        // Arrange
        UserDTO dto = new UserDTO(null, "test@example.com", "wrongPassword");
        User user = new User("testUser", "test@example.com", "encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act
        String result = userService.login(dto);

        // Assert
        assertEquals("Invalid email or password!", result);
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }
}