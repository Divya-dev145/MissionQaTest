@api
Feature: API test

  Scenario: Should see LIST USERS of all existing users
    Given I get the default list of users for on 1st page
    When I get the list of all users within every page
    Then I should see total users count equals the number of user ids

  Scenario: Should see SINGLE USER data
    Given I make a search for user 3
    Then I should see the following user data
      | first_name | email               |
      | Emma       | emma.wong@reqres.in |

  Scenario: Should see SINGLE USER NOT FOUND error code
    Given I make a search for user 55
    Then I receive error code 404 in response

  Scenario Outline: CREATE a user
    Given I create a user with following "<name>" "<job>"
    Then response should contain the following data
      | name | job |
    Examples:
      | name   | job      |
      | Divya  | QA Lead  |
      Scenario Outline: UPDATE a user completely using PUT
    Given I update user 2 with name "<name>" and job "<job>" using PUT
    Then the API status code should be 200
    And response should contain the updated data
      | name   | job   |
      | <name> | <job> |  # <--- Added this line to fix the error
    Examples:
      | name         | job       |
      | Divya Lakshmi | Senior QA |
      
  Scenario: UPDATE a user partially using PATCH
    Given I update user 2 with job "Architect" using PATCH
    Then the API status code should be 200
    And response should contain "job" as "Architect"

  Scenario: DELETE a user
    When I delete user 2
    Then the API status code should be 204