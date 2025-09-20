Feature: User Authentication
  As a user
  I want to be able to register and login
  So that I can access the application securely

  Background:
    Given the application is running
    And I have a clean user database

  @Register
  Scenario: Successful user registration
    When I send a POST request to "/api/auth/signup" with:
      | username | testuser     |
      | email    | test@example.com |
      | password | password123  |
    Then the response status should be 200
    And the response should contain "User registered successfully!"

  @Register
  Scenario: Registration with existing username
    Given a user already exists with username "existinguser" and email "existing@example.com"
    When I send a POST request to "/api/auth/signup" with:
      | username | existinguser |
      | email    | new@example.com |
      | password | password123  |
    Then the response status should be 400
    And the response should contain "Username already exists!"

  @Login
  Scenario: Successful login
    Given a user exists with username "loginuser" and email "login@example.com" and password "password123"
    When I send a POST request to "/api/auth/login" with:
      | email    | login@example.com |
      | password | password123      |
    Then the response status should be 200
    And the response should contain "Login successful!"

  @Login
  Scenario: Login with invalid credentials
    Given a user exists with username "loginuser" and email "login@example.com" and password "password123"
    When I send a POST request to "/api/auth/login" with:
      | email    | login@example.com |
      | password | wrongpassword    |
    Then the response status should be 401
    And the response should contain "Invalid email or password!"