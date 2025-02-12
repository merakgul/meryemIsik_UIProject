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
import pages.HepsiburadaLocators;
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
        WebElement electronicsCategory = methods.waitForElement(HepsiburadaLocators.ELECTRONICS_CATEGORY);
        electronicsCategory.click();

        Actions action = new Actions(driver);
        WebElement pcTabletCategory = methods.waitForElement(HepsiburadaLocators.PC_TABLET_CATEGORY);
        action.moveToElement(pcTabletCategory).perform();

        WebElement tabletCategory = methods.waitForElement(HepsiburadaLocators.TABLET_CATEGORY);
        tabletCategory.click();
    }

    @When("The user applies filters for Brand -> {string} and Screen Size -> {string}")
    public void applyFilters(String brand, String screenSize) {
        WebElement brandFilter = methods.waitForElement(By.xpath("//span[text()='" + brand + "']//parent::a"));
        brandFilter.click();

        WebElement screenSizeFilter = methods.waitForElement(By.xpath("//span[text()='" + screenSize + "']//parent::a"));
        screenSizeFilter.click();
    }

    @When("The user clicks on the highest priced product")
    public void clickOnMostExpensiveProduct() {
        List<WebElement> priceElements = driver.findElements(HepsiburadaLocators.PRODUCT_PRICES);

        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace(" TL", "").replace(".", "").replace(",", ".");
            double price = Double.parseDouble(priceText);
            if (price > maxPrice) {
                maxPrice = price;
                highestPriceProductContainer = priceElement.findElement(HepsiburadaLocators.PRODUCT_CONTAINER);
            }
        }
        System.out.println("Max price: " + maxPrice + " TL");
    }

    @When("The user adds the product to the cart")
    public void addToCart() {
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
            WebElement addToCartButton = methods.waitForElement(HepsiburadaLocators.ADD_TO_CART_BUTTON);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", addToCartButton);
            System.out.println("Highest price product added to the cart");

            driver.switchTo().window(mainWindowHandle);
        } else {
            System.out.println("Highest price product could not be found.");
        }
        waitFor(5000);
    }

    public void waitFor(Integer duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Then("The user verifies that the product is added to the cart and the price is correct")
    public void verifyProductInCart() {
        WebElement cartIcon = methods.waitForElement(HepsiburadaLocators.CART_ICON);
        cartIcon.click();

        WebElement cartPriceElement = methods.waitForElementToBeVisible(HepsiburadaLocators.CART_PRICE);
        String cartPriceText = cartPriceElement.getText().replace(" TL", "").trim();

        cartPriceText = cartPriceText.replaceAll(",", "\\.");

        cartPriceText = cartPriceText.replaceAll("\\.(?=.*\\.)", "");

        double cartPrice = Double.parseDouble(cartPriceText);

        assertEquals("The price in the cart does not match the expected price!", maxPrice, cartPrice, 0.01);
        System.out.println("Product added to cart and price is correct: " + cartPrice + " TL");
    }

    @After
    public static void tearDown() {
        MyDriver.closeDriver();
    }
}
