package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.Methods;
import utilities.MyDriver;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class HepsiburadaTest {
    private static WebDriver driver;
    private WebElement highestPriceProductContainer = null;
    private double maxPrice = 0.0;
    public Methods methods = new Methods(driver);

    @Before
    public static void setUp() {
        driver = MyDriver.getDriver();
    }

    @Given("The user navigates to the Hepsiburada website")
    public void navigateToHepsiburada() {
        driver.get("https://www.hepsiburada.com/");
        driver.manage().window().maximize();
    }

    @When("The user navigates to the Electronics -> Tablet category")
    public void navigateToTabletCategory() {
        WebElement electronicsCategory = methods.waitForElement(By.xpath("//*[text()='Elektronik']//parent::*[@class='sf-MenuItems-UHHCg2qrE5_YBqDV_7AC']"));
        electronicsCategory.click();

        Actions action = new Actions(driver);
        WebElement pcTabletCategory = methods.waitForElement(By.xpath("//a[text()='Bilgisayar/Tablet']//parent::li"));
        action.moveToElement(pcTabletCategory).perform();

        WebElement tabletCategory = methods.waitForElement(By.xpath("//a[text()='Tablet']//parent::li"));
        tabletCategory.click();
    }

    @When("The user applies filters for Brand -> Apple and Screen Size -> 13.2 inches")
    public void applyFilters() {
        WebElement brandFilter = methods.waitForElement(By.xpath("//span[text()='Apple']//parent::a"));
        brandFilter.click();

        WebElement screenSizeFilter = methods.waitForElement(By.xpath("//span[text()='13,2 inç']//parent::a"));
        screenSizeFilter.click();
    }

    @When("The user clicks on the highest priced product")
    public void clickOnMostExpensiveProduct() {
        // Get all product prices on the page
        List<WebElement> priceElements = driver.findElements(By.xpath("//div[@data-test-id='price-current-price']"));

        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace(" TL", "").replace(".", "").replace(",", ".");
            double price = Double.parseDouble(priceText);
            if (price > maxPrice) {
                maxPrice = price;
                highestPriceProductContainer = priceElement.findElement(By.xpath(".//ancestor::li[contains(@type, 'comfort')]"));
            }
        }
        System.out.println("Max price: " + maxPrice + " TL");
    }

    @When("The user adds the product to the cart")
    public void addToCart() throws InterruptedException {
        if (highestPriceProductContainer != null) {
            methods.hoverOverElement(highestPriceProductContainer);
            highestPriceProductContainer.click();

            String mainWindowHandle = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(mainWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            WebElement addToCartButton = methods.waitForElement(By.xpath("//*[@data-test-id='addToCart']"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", addToCartButton);
            System.out.println("Highest price product added to the cart");

            driver.switchTo().window(mainWindowHandle);
        } else {
            System.out.println("Highest price product could not be found.");
        }
        Thread.sleep(5000);
    }

    @Then("The user verifies that the product is added to the cart and the price is correct")
    public void verifyProductInCart() {
        WebElement cartIcon = methods.waitForElement(By.id("shoppingCart"));
        cartIcon.click();

        WebElement cartPriceElement = methods.waitForElementToBeVisible(By.xpath("//div[@class='product_price_uXU6Q']"));
        String cartPriceText = cartPriceElement.getText().replace(" TL", "").trim();

        cartPriceText = cartPriceText.replaceAll(",", "\\.");  // Replace the first comma if multiple commas are there

        cartPriceText = cartPriceText.replaceAll("\\.(?=.*\\.)", "");  // Remove any extra dots except the last one (in case of decimal points)

        double cartPrice = Double.parseDouble(cartPriceText);

        assertEquals("The price in the cart does not match the expected price!", maxPrice, cartPrice, 0.01);
        System.out.println("Product added to cart and price is correct: " + cartPrice + " TL");
    }

    @After
    public static void tearDown() {
        MyDriver.closeDriver();
    }
}
