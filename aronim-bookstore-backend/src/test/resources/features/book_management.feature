Feature: Book Management
  As a bookstore administrator
  I want to manage the book inventory
  So that customers can browse and purchase books

  Background:
    Given the system has an admin user with email "admin@localhost.local" and password "changeme"
    And I am logged in as "admin@localhost.local" with password "changeme"
    And there are no existing books in the system

  Scenario: Create a new book
    When I create a book with the following details:
      | title           | Clean Code                      |
      | isbn            | 9780132350884                   |
      | authorFirstName | Robert                          |
      | authorLastName  | Martin                          |
      | publisher       | Prentice Hall                   |
      | price           | 39.99                           |
      | stock           | 10                              |
    Then the response status should be 201
    And the book with ISBN "9780132350884" should be available in the catalog

  Scenario: Get all books
    Given the following books exist in the catalog:
      | title           | isbn           | authorFirstName | authorLastName | publisher     | price | stock |
      | Clean Code      | 9780132350884  | Robert         | Martin        | Prentice Hall | 39.99 | 10    |
      | Clean Coder     | 9780137081073  | Robert         | Martin        | Prentice Hall | 29.99 | 5     |
    When I request all books
    Then the response status should be 200
    And the response should contain 2 books
    And the response should include a book with ISBN "9780132350884"
    And the response should include a book with ISBN "9780137081073"

  Scenario: Update book stock
    Given a book with ISBN "9780132350884" exists in the catalog with 10 items in stock
    When I update the stock of the book with ISBN "9780132350884" to 15
    Then the response status should be 200
    And the book with ISBN "9780132350884" should have 15 items in stock

  Scenario: Update book price
    Given a book with ISBN "9780132350884" exists in the catalog with price 39.99
    When I update the price of the book with ISBN "9780132350884" to 44.99
    Then the response status should be 200
    And the book with ISBN "9780132350884" should have price 44.99
