package com.example.demo.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginPageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless"); // ADD THIS FOR CI
        options.addArguments("--remote-allow-origins=*"); // ADD THIS

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach // ADD THIS METHOD
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFailedLogin() {
        driver.get("http://localhost:5173/logging");

        WebElement emailInput = driver.findElement(By.cssSelector("input[name='email']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Login now')]"));

        emailInput.sendKeys("invalid@example.com");
        passwordInput.sendKeys("wrongpassword");
        loginButton.click();

        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'bg-red-100')]")));

        assertTrue(errorElement.isDisplayed(), "Error message should be displayed for failed login");
        assertTrue(errorElement.getText().contains("Login failed") ||
                        errorElement.getText().contains("Invalid"),
                "Error message should indicate login failure");
    }
}