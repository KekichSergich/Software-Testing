package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SearchProductTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    private void performSearch(String input) {
        driver.get("https://www.datart.cz");
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.sendKeys(input);
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".search-suggestion")));
    }

    private List<WebElement> getSuggestions() {
        return driver.findElements(By.cssSelector(".search-suggestion-product__name"));
    }

    @Test
    public void testTC01_validNameFromHomepage() {
        performSearch("notebook");
        boolean found = getSuggestions().stream()
                .anyMatch(el -> el.getText().toLowerCase().contains("notebook"));

        System.out.println("[TC01] Input: 'notebook' from homepage");
        System.out.println("Expected: Matching suggestions should appear");
        System.out.println("Actual: " + (found ? "Suggestions found" : "No matching suggestions"));
        assertTrue(found, "Expected valid product suggestions.");
        System.out.println("Result: PASSED\n");
    }

    @Test
    public void testTC02_nonexistentFromCategory() {
        driver.get("https://www.datart.cz/notebooky.html");
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.sendKeys("asdfghqwerty123456");
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".search-suggestion")));
        List<WebElement> suggestions = getSuggestions();
        boolean found = suggestions.stream()
                .anyMatch(el -> el.getText().toLowerCase().contains("asdfghqwerty123456"));

        System.out.println("[TC02] Input: 'asdfghqwerty123456' from category");
        System.out.println("Expected: No suggestions or no matching ones");
        System.out.println("Actual: " + (found ? "Unexpected match found" : "No match found"));
        assertTrue(suggestions.isEmpty() || !found, "Expected no matching results.");
        System.out.println("Result: PASSED\n");
    }

    @Test
    public void testTC03_emptyInput() {
        driver.get("https://www.datart.cz");
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.sendKeys("");
        List<WebElement> suggestions = getSuggestions();

        System.out.println("[TC03] Input: empty string");
        System.out.println("Expected: No suggestions should appear");
        System.out.println("Actual: " + (suggestions.isEmpty() ? "No suggestions" : "Some suggestions displayed"));
        assertTrue(suggestions.isEmpty(), "Expected no suggestions for empty input.");
        System.out.println("Result: PASSED\n");
    }

    @Test
    public void testTC04_numericInput() {
        driver.get("https://www.datart.cz");
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.sendKeys("123456");
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".search-suggestion")));
        List<WebElement> suggestions = getSuggestions();
        boolean found = suggestions.stream().anyMatch(el -> el.getText().contains("123456"));

        System.out.println("[TC04] Input: '123456' (numeric)");
        System.out.println("Expected: No suggestions or no matching ones");
        System.out.println("Actual: " + (found ? "Unexpected match found" : "No match found"));
        assertTrue(suggestions.isEmpty() || !found, "Expected no suggestions for numeric input.");
        System.out.println("Result: PASSED\n");
    }

    @Test
    public void testTC05_specialCharacters() {
        driver.get("https://www.datart.cz");
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.sendKeys("!@#$%^&*");
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".search-suggestion")));
        List<WebElement> suggestions = getSuggestions();
        boolean found = suggestions.stream().anyMatch(el -> el.getText().contains("!@#$%^&*"));

        System.out.println("[TC05] Input: '!@#$%^&*' (special characters)");
        System.out.println("Expected: No suggestions or no matching ones");
        System.out.println("Actual: " + (found ? "Unexpected match found" : "No match found"));
        assertTrue(suggestions.isEmpty() || !found, "Expected no suggestions for special characters.");
        System.out.println("Result: PASSED\n");
    }

    @Test
    public void testTC06_validNameFromCategory() {
        driver.get("https://www.datart.cz/notebooky.html");
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='search']"));
        searchBox.sendKeys("notebook");
        new WebDriverWait(driver, Duration.ofSeconds(7))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".search-suggestion-product__name")));
        boolean found = getSuggestions().stream()
                .anyMatch(el -> el.getText().toLowerCase().contains("notebook"));

        System.out.println("[TC06] Input: 'notebook' from category page");
        System.out.println("Expected: Matching suggestions should appear");
        System.out.println("Actual: " + (found ? "Suggestions found" : "No matching suggestions"));
        assertTrue(found, "Expected valid product suggestions from category page.");
        System.out.println("Result: PASSED\n");
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
