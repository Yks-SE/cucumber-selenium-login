
package com.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks {
    public static WebDriver driver;

    @Before
    public void setUp() {
    WebDriverManager.chromedriver().browserVersion("140.0.7339.208").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    @After
    public void tearDown(Scenario scenario) {
        // Take screenshot if scenario failed
        if (scenario.isFailed() && driver != null) {
            try {
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
            } catch (Exception e) {
                System.err.println("Screenshot capture failed: " + e.getMessage());
            }
        }
        if (driver != null) {
            try {
                Thread.sleep(1000); // give Chrome some time to finish network ops
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error during driver quit: " + e.getMessage());
            }
        }
    }
}
