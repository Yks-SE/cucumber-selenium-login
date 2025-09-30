package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By flash = By.id("flash");
    private final By secureAreaHeader = By.cssSelector("div.example h2");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://the-internet.herokuapp.com/login");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.id("username")));
    }

    public void setUsername(String username) {
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }

    public void setPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public String getFlashMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement flashElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash")));
            // Small pause to ensure text is fully loaded
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.textToBePresentInElement(flashElement, ""));
            return flashElement.getText().trim();
        } catch (Exception e) {
            System.err.println("[DEBUG] Could not find #flash element. Current URL: " + driver.getCurrentUrl());
            System.err.println("[DEBUG] Page source:\n" + driver.getPageSource());
            return "FLASH_NOT_FOUND";
        }
    }

    public boolean isInSecureArea() {
        return driver.getCurrentUrl().contains("/secure")
                && driver.findElement(secureAreaHeader).getText().toLowerCase().contains("secure area");
    }

    public String getUsernameValidationMessage() {
        return driver.findElement(usernameInput).getAttribute("validationMessage");
    }

    public String getPasswordValidationMessage() {
        return driver.findElement(passwordInput).getAttribute("validationMessage");
    }
}