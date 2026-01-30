package mission;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String user, String pass) {
        driver.findElement(By.id("user-name")).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.id("login-button")).click();
    }

    public void addItem(String itemName) {
        String xpath = String.format(
                "//div[text()='%s']/ancestor::div[@class='inventory_item']//button", itemName);
        driver.findElement(By.xpath(xpath)).click();
    }

    public void removeItem(String itemName) {
        String xpath = "//div[@class='inventory_item_name' and text()='" + itemName + "']" +
                       "/ancestor::div[@class='cart_item']//button[contains(@id,'remove')]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        removeBtn.click();
    }
    public void sortProducts(String option) {
        WebElement sortDropDown = driver.findElement(By.className("product_sort_container"));
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(sortDropDown);
        select.selectByVisibleText(option);
    }
}
