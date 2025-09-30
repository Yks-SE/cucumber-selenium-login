
@smoke
Feature: Login functionality - the-internet.herokuapp.com

  Background:
    Given the user navigates to the login page

  Scenario: Valid Login
    When the user enters username "tomsmith" and password "SuperSecretPassword!"
    Then the user should be successfully logged in and redirected to the Secure Area page

  Scenario: Invalid Login
    When the user enters username "wrongusername" and password "wrongpassword"
    Then an error message "Your username is invalid!" should be displayed

  Scenario: Empty Fields
    When the user leaves both the username and password fields empty and submits
    Then a required field message should be displayed for both fields

  @datadriven
  Scenario Outline: Login attempts using examples
    When the user enters username "<username>" and password "<password>"
    Then <result>

    Examples:
      | username   | password               | result                                                                 |
      | tomsmith   | SuperSecretPassword!   | the user should be successfully logged in and redirected to the Secure Area page |
      | tomsmith   | wrongpassword          | an error message "Your password is invalid!" should be displayed       |
      | wronguser  | SuperSecretPassword!   | an error message "Your username is invalid!" should be displayed       |

  @csv
  Scenario: Login attempts using CSV (bonus)
    Given test data from csv "data/login.csv"
    When I attempt login with every row in the csv "data/login.csv"
    Then all csv logins should match the expected outcomes
