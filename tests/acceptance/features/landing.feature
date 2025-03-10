Feature: Landing page
  Scenario: User navigates to Landing page
    Given that user is not logged in
    When the user navigates to the Landing page
    Then the user should be presented with a page titled "Refine"
