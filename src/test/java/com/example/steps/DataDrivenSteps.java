
package com.example.steps;

import com.example.pages.LoginPage;
import com.example.utils.CsvData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DataDrivenSteps {
    private final WebDriver driver;
    private final LoginPage loginPage;

    public DataDrivenSteps() {
        this.driver = Hooks.driver;
        this.loginPage = new LoginPage(driver);
    }

    @Given("test data from csv {string}")
    public void test_data_from_csv(String csvPath) {
        // Load data now so it's ready for the next steps; kept for readability.
        // Actual iteration happens in the next step.
        CsvData.cache(csvPath);
    }

    @When("I attempt login with every row in the csv {string}")
    public void i_attempt_login_with_every_row_in_the_csv(String csvPath) {
        List<Map<String, String>> rows = CsvData.read(csvPath);
        for (Map<String, String> row : rows) {
            loginPage.open();
            String username = row.get("username");
            String password = row.get("password");
            loginPage.setUsername(username);
            loginPage.setPassword(password);
            loginPage.clickLogin();

            String expected = row.get("expected");
            if ("success".equalsIgnoreCase(expected)) {
                Assertions.assertTrue(loginPage.isInSecureArea(),
                        "Expected success for row: " + row);
            } else if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
                // Check required field validation messages or that user remains on login page
                String usernameMsg = loginPage.getUsernameValidationMessage();
                String passwordMsg = loginPage.getPasswordValidationMessage();
                boolean hasValidationMsg = !usernameMsg.isEmpty() || !passwordMsg.isEmpty();
                boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
                Assertions.assertTrue(hasValidationMsg || stillOnLoginPage,
                        "Expected required field message or to remain on login page for row: " + row + ", usernameMsg: " + usernameMsg + ", passwordMsg: " + passwordMsg + ", url: " + driver.getCurrentUrl());
            } else {
                String flash = loginPage.getFlashMessage();
                boolean flashInvalid = flash.toLowerCase().contains("invalid");
                boolean stillOnLoginPage = driver.getCurrentUrl().contains("/login");
                Assertions.assertTrue(flashInvalid || stillOnLoginPage,
                        "Expected an invalid message or to remain on login page for row: " + row + " but got: " + flash + ", url: " + driver.getCurrentUrl());
            }
        }
    }

    @Then("all csv logins should match the expected outcomes")
    public void all_csv_logins_should_match_the_expected_outcomes() {
        // Assertions are done inside the loop; this step is for readability.
    }
}
