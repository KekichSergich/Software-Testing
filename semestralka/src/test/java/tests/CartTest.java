package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void testAddProductToCart() {
        // Step 1: Open product detail page
        driver.get("https://www.datart.cz/notebook-asus-vivobook-15-x1504va-nj124w-modry.html");

        // Step 2: Close cookie and survey banners if they appear
        try {
            WebElement cookieBanner1 = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(By.id("c-s-bn")));
            cookieBanner1.click();
        } catch (Exception ignored) {}

        try {
            WebElement cookieBanner2 = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(By.id("s-sv-bn")));
            cookieBanner2.click();
        } catch (Exception ignored) {}

        // Step 3: Click "Add to cart" button
        WebElement addToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-lb-action='buy']")));
        addToCartButton.click();

        // Step 4: Wait for confirmation modal
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".basket-product-add")));

        // Step 5: Click "Go to cart"
        WebElement goToCartButton = driver.findElement(By.cssSelector("a.btn.btn-intoBasket"));
        goToCartButton.click();

        // Step 6: Wait for cart page to load
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/kosik"));

        // Step 7: Check if product is in cart
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".basket-product-wrap"));
        assertTrue(cartItems.size() > 0, "Cart is empty after adding a product.");

        System.out.println("Test passed: Product successfully added to cart.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
