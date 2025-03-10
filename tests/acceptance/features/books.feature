Feature: Create Book

  Scenario: Admin creates a book
    Given that user is signed in using username "admin@aronim.local" and password "P@ssw0rd"
    When the user clicks the Create button
    And the user enters the following information
      | ISBN          | Title            | Author's First Name | Author's Last Name | Publisher's Name     | Price |
      | 9781617294945 | Spring in Action | Craig               | Walls              | Manning Publications | 49.99 |
    And the user clicks the Save button
