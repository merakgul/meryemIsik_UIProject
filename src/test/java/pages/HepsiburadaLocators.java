package pages;

import org.openqa.selenium.By;

public class HepsiburadaLocators {

    public static final By ELECTRONICS_CATEGORY = By.xpath("//*[text()='Elektronik']//parent::*[@class='sf-MenuItems-UHHCg2qrE5_YBqDV_7AC']");
    public static final By PC_TABLET_CATEGORY = By.xpath("//a[text()='Bilgisayar/Tablet']//parent::li");
    public static final By TABLET_CATEGORY = By.xpath("//a[text()='Tablet']//parent::li");
    public static final By PRODUCT_PRICES = By.xpath("//div[@data-test-id='price-current-price']");
    public static final By PRODUCT_CONTAINER = By.xpath(".//ancestor::li[contains(@type, 'comfort')]");
    public static final By ADD_TO_CART_BUTTON = By.xpath("//*[@data-test-id='addToCart']");
    public static final By CART_ICON = By.id("shoppingCart");
    public static final By CART_PRICE = By.xpath("//div[@class='product_price_uXU6Q']");
}