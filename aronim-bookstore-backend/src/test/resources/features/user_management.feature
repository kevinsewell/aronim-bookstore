Feature: User Management
  As a system administrator
  I want to manage user accounts
  So that users can have appropriate access to the bookstore

  Scenario: Register a new user
    When I register a new user with the following details:
      | email           | john.doe@example.com |
      | password        | securePassword123    |
      | firstName       | John                 |
      | lastName        | Doe                  |
    Then the response status should be 201
    And the user with email "john.doe@example.com" should exist in the system

  Scenario: User login
    Given a user exists with email "john.doe@example.com" and password "securePassword123"
    When I login with email "john.doe@example.com" and password "securePassword123"
    Then the response status should be 200
    And the response should contain an authentication token
