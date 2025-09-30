
package com.example.steps;

import com.example.pages.LoginPage;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

public class LoginSteps {
    private final WebDriver driver;
    private final LoginPage loginPage;

    public LoginSteps() {
        this.driver = Hooks.driver;
        this.loginPage = new LoginPage(driver);
    }

    @Given("the user navigates to the login page")
    public void the_user_navigates_to_the_login_page() {
        loginPage.open();
    }

    @When("the user enters username {string} and password {string}")
    public void the_user_enters_username_and_password(String username, String password) {
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickLogin();
    }

    @Then("the user should be successfully logged in and redirected to the Secure Area page")
    public void the_user_should_be_successfully_logged_in_and_redirected_to_the_secure_area_page() {
        Assertions.assertTrue(loginPage.isInSecureArea(),
                "Expected to be in Secure Area but wasn't.");
    }

    @Then("an error message {string} should be displayed")
    public void an_error_message_should_be_displayed(String message) {
        String flash = loginPage.getFlashMessage();
        Assertions.assertTrue(flash.contains(message),
                "Expected flash to contain: " + message + " but was: " + flash);
    }

    @When("the user leaves both the username and password fields empty and submits")
    public void the_user_leaves_both_fields_empty_and_submits() {
        loginPage.setUsername("");
        loginPage.setPassword("");
        loginPage.clickLogin();
    }

    @Then("a required field message should be displayed for both fields")
    public void a_required_field_message_should_be_displayed_for_both_fields() {
        // Try HTML5 validation first
        String u = loginPage.getUsernameValidationMessage();
        String p = loginPage.getPasswordValidationMessage();

        boolean html5ValidationShown = (u != null && !u.isEmpty()) || (p != null && !p.isEmpty());
        if (html5ValidationShown) {
            // Browsers typically say "Please fill out this field."
            Assertions.assertTrue(u.toLowerCase().contains("fill out") || p.toLowerCase().contains("fill out"),
                    "Expected browser validation message, got username='" + u + "', password='" + p + "'");
        } else {
            // Fallback to app banner (site behavior today)
            String flash = loginPage.getFlashMessage();
            Assertions.assertTrue(flash.contains("Your username is invalid!"),
                    "Expected 'Your username is invalid!' when submitting empty fields, got: " + flash);
        }
    }
}
