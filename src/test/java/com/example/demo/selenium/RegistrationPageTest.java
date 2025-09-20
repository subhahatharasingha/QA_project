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

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationPageTest {

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
    public void testSuccessfulRegistration() {
        driver.get("http://localhost:5173/signup");

        // Wait for page to load
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

        // Generate unique test data
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "testuser" + timestamp;
        String email = "test" + timestamp + "@example.com";

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='username']")));
        usernameInput.clear();
        usernameInput.sendKeys(username);

        WebElement emailInput = driver.findElement(By.cssSelector("input[name='email']"));
        emailInput.clear();
        emailInput.sendKeys(email);

        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        passwordInput.clear();
        passwordInput.sendKeys("password123");

        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(), 'Signup now')]"));
        signupButton.click();

        wait.until(ExpectedConditions.urlContains("/logging"));
        assertTrue(driver.getCurrentUrl().contains("/logging"), "Should be redirected to login page after registration");
    }
}