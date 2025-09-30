public String getUsernameValidationMessage() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(usernameInput));
    return usernameField.getAttribute("validationMessage");
}

public String getPasswordValidationMessage() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(passwordInput));
    return passwordField.getAttribute("validationMessage");
}