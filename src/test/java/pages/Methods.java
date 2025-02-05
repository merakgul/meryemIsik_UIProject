package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Methods {
    private final WebDriver driver;

    public Methods(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void hoverOverElement(WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).perform();
    }

    public void clickKeyboard(WebElement webElement, Keys keyboard) {
        webElement.sendKeys(keyboard);
    }

    public void scrollIntoView(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

}
