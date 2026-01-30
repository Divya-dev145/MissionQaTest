@ui
Feature: UI test

  Scenario: Complete order with two products and verify totals
    Given user is on the login page
    And I login in with the following details
      | userName      | Password     |
      | standard_user | secret_sauce |
    And I add the following items to the basket
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And I  should see 2 items added to the shopping cart
    And I click on the shopping cart
    And I verify that the QTY count for each item should be 1
    And I remove the following item:
      | Sauce Labs Bike Light |
    And I  should see 1 items in the cart page
    And I accept the change password popup if present
    And I click on the CHECKOUT button
    And I type "Varun" for First Name
    And I type "Kumar" for Last Name
    And I type "78222" for ZIP/Postal Code
    When I click on the CONTINUE button
    Then Item total will be equal to the total of items on the list
    And a Tax rate of 8 % is applied to the total
  Scenario: Verify product sorting by price Low to High
    And I login in with the following details
      | userName      | Password     |
      | standard_user | secret_sauce |
    When I sort products by "Price (low to high)"
    Then the products should be displayed in ascending price order

  Scenario Outline: Verify login error messages for invalid users
    When I login in with the following details
      | userName | Password |
      | <user>   | <pass>   |
    Then I should see the error message "<errorMessage>"
    Examples:
      | user            | pass         | errorMessage                                               |
      | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.        |
      | standard_user   | wrong_pass   | Epic sadface: Username and password do not match any user  |
    