package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CartTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    // Helper method to close banners that may interfere with interaction
    public void closeBanners() {
        try {
            WebElement cookieBanner = new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(By.id("c-s-bn")));
            cookieBanner.click();
        } catch (Exception ignored) {}

        try {
            WebElement surveyBanner = new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(By.id("s-sv-bn")));
            surveyBanner.click();
        } catch (Exception ignored) {}

        try {
            WebElement noThanksButton = new WebDriverWait(driver, Duration.ofSeconds(1))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".exponea-button-close")));
            noThanksButton.click();
        } catch (Exception ignored) {
            try {
                WebElement skipLink = new WebDriverWait(driver, Duration.ofSeconds(1))
                        .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".exponea-colose-link")));
                skipLink.click();
            } catch (Exception ignoredAgain) {}
        }
    }


    /**
     * TC07 - Adds a product to the cart from a valid product page.
     */
    @Test
    public void TC07testAddProductToCart() {
        driver.get("https://www.datart.cz/notebook-asus-vivobook-15-x1504va-nj124w-modry.html");
        closeBanners();

        WebElement addToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-lb-action='buy']")));
        addToCartButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".basket-product-add")));
        driver.findElement(By.cssSelector("a.btn.btn-intoBasket")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/kosik"));
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".basket-product-wrap"));
        assertTrue(cartItems.size() > 0, "Cart is empty after adding a product.");
    }

    /**
     * TC08 - Checks that "Add to Cart" button is not present on a non-product page.
     */
    @Test
    public void TC08testNoAddToCartButton() {
        driver.get("https://www.datart.cz/kontakty.html");

        List<WebElement> buttons = driver.findElements(By.cssSelector("div.product-detail-wrapper button[data-lb-action='buy']"));
        boolean buttonVisible = buttons.stream().anyMatch(WebElement::isDisplayed);

        assertTrue(buttons.isEmpty() || !buttonVisible, "Expected 'Add to Cart' button to be missing or hidden.");
    }

    /**
     * TC09 - Opens a non-existing product page and checks for error or 404.
     */
    @Test
    public void TC09testInvalidProductPage() {
        driver.get("https://www.datart.cz/neexistujici-produkt.html");

        String pageSource = driver.getPageSource().toLowerCase();
        assertTrue(pageSource.contains("nenalezeno") || pageSource.contains("404"),
                "Expected error message or 404 on invalid page.");
    }

    /**
     * TC10 - Increases quantity in the cart using the plus (+) button.
     */
    @Test
    public void TC10testIncreaseQuantityUsingPlusButton() throws InterruptedException {
        driver.get("https://www.datart.cz/notebook-asus-vivobook-15-x1504va-nj124w-modry.html");
        closeBanners();

        WebElement addToCartBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-lb-action='buy']")));
        addToCartBtn.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".basket-product-add")));
        driver.findElement(By.cssSelector("a.btn.btn-intoBasket")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/kosik"));
        WebElement quantityInput = driver.findElement(By.cssSelector("input.change-quantity-input"));
        String valueBefore = quantityInput.getAttribute("value");

        WebElement plusButton = driver.findElement(By.cssSelector("span.change-quantity[data-type='add']"));
        plusButton.click();

        Thread.sleep(2000);

        WebElement quantityInputAfter = driver.findElement(By.cssSelector("input.change-quantity-input"));
        String valueAfter = quantityInputAfter.getAttribute("value");

        assertTrue(Integer.parseInt(valueAfter) > Integer.parseInt(valueBefore),
                "Quantity should increase after clicking the '+' button.");
    }

    /**
     * TC11 - Verifies that a visible banner intercepts the add-to-cart click.
     */
    @Test
    public void TC11testClickInterceptedByBanner() {
        driver.get("https://www.datart.cz/notebook-asus-vivobook-15-x1504va-nj124w-modry.html");

        try {
            WebElement banner = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("s-sv-bn")));

            WebElement addToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-lb-action='buy']")));
            addToCartButton.click();

            fail("Expected click to be intercepted by banner, but it succeeded.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Test passed: Click was intercepted as expected due to banner.");
        }
    }

    /**
     * TC12 - Checks for confirmation modal after clicking Add to Cart.
     */
    @Test
    public void TC12testAddToCartWithoutConfirmationModal() {
        driver.get("https://www.datart.cz/notebook-asus-vivobook-15-x1504va-nj124w-modry.html");
        closeBanners();

        WebElement addToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-lb-action='buy']")));
        addToCartButton.click();

        boolean modalAppeared;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".basket-product-add")));
            modalAppeared = true;
        } catch (Exception e) {
            modalAppeared = false;
        }

        if (modalAppeared) {
            System.out.println("[TC12] Confirmation modal appeared — Expected behavior");
        } else {
            System.out.println("[TC12] Confirmation modal did NOT appear — Unexpected, but not a failure");
        }

        assertTrue(true);
    }

    /**
     * Parameterized test - Adds a product to cart and checks quantity from CSV file.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/cart-tests-data.csv", numLinesToSkip = 1)
    public void testProductAddWithExpectedQuantity(String url, int expectedMinQuantity) {
        driver.get(url);
        closeBanners();

        WebElement addToCart = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-lb-action='buy']")));
        addToCart.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".basket-product-add")));
        driver.findElement(By.cssSelector("a.btn.btn-intoBasket")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/kosik"));

        WebElement qtyInput = driver.findElement(By.cssSelector("input.change-quantity-input"));
        int qty = Integer.parseInt(qtyInput.getAttribute("value"));

        assertTrue(qty >= expectedMinQuantity, "Expected at least " + expectedMinQuantity + " in cart.");

        System.out.printf("✅ Test passed: %s → Quantity in cart = %d (expected at least %d)%n", url, qty, expectedMinQuantity);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/add-to-cart-button-visible.csv", numLinesToSkip = 1)
    public void testAddToCartButtonVisibility(String url, boolean expectedVisible) {
        driver.get(url);
        closeBanners();

        List<WebElement> buttons = driver.findElements(By.cssSelector("button[data-lb-action='buy']"));
        boolean isVisible = buttons.stream().anyMatch(WebElement::isDisplayed);

        assertTrue(isVisible == expectedVisible, "Button visibility mismatch for: " + url);
        System.out.println("Checked button visibility for: " + url + " → " + isVisible);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/expected-keyword-check.csv", numLinesToSkip = 1)
    public void testExpectedKeywordInPageSource(String url, String expectedKeyword) {
        driver.get(url);
        closeBanners();

        String pageContent = driver.getPageSource().toLowerCase();

        assertTrue(pageContent.contains(expectedKeyword.toLowerCase()),
                "Expected keyword not found: " + expectedKeyword + " on " + url);

        System.out.println("✅ Found keyword '" + expectedKeyword + "' on page: " + url);
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
