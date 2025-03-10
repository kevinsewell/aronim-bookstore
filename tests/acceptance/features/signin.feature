Feature: Sign in page
  Scenario: Admin user signs into Aronim Bookstore
    Given the user has navigated to the Landing page
    And the user has clicked on the Sign in button
    And the user has been presented with a page title "Sign in to Aronim Bookstore"
    When signs in with username "admin@aronim.local" and password "P@ssw0rd"
    Then the user should be presented with a page titled "Books | Refine"
    And the page should display the user's full name "Admin User"

  Scenario: Standard user signs into Aronim Bookstore
    Given the user has navigated to the Landing page
    And the user has clicked on the Sign in button
    And the user has been presented with a page title "Sign in to Aronim Bookstore"
    When signs in with username "user@aronim.local" and password "P@ssw0rd"
    Then the user should be presented with a page titled "Books | Refine"
    And the page should display the user's full name "Standard User"
