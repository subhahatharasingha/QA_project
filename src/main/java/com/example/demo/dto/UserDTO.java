package com.example.demo.dto;

public class UserDTO {
    private String username;
    private String email;
    private String password;

    // No-args constructor (needed by Spring / Jackson)
    public UserDTO() {
    }

    // All-args constructor (needed for tests or manual creation)
    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
