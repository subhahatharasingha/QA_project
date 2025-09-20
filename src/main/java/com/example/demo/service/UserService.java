package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(UserDTO dto) {
        validateRegistration(dto);

        User user = createUserFromDTO(dto);
        userRepository.save(user);

        return "User registered successfully!";
    }

    public String login(UserDTO dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

        if (userOptional.isEmpty() || !isPasswordValid(dto, userOptional.get())) {
            return "Invalid email or password!";
        }

        return "Login successful!";
    }

    private void validateRegistration(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
    }

    private User createUserFromDTO(UserDTO dto) {
        return new User(
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword())
        );
    }

    private boolean isPasswordValid(UserDTO dto, User user) {
        return passwordEncoder.matches(dto.getPassword(), user.getPassword());
    }
}