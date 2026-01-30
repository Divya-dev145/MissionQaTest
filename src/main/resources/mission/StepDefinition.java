package mission;

import io.cucumber.java.After
;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class StepDefinition extends BasePage {
    BrowserSetup browserSetup = new BrowserSetup();
    HomePage homePage;
    WebDriverWait wait;

    @Before
    public void setUp() {
        browserSetup.selectBrowser();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(LoadProp.getProperty("url"));  // Assumes TestData.properties has "url=saucedemo.com"
        homePage = new HomePage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("user is on the login page")
    public void userIsOnLoginPage() {
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Swag Labs"), "Not on login page");
    }

    @When("user enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String user, String pass) {
        homePage.login(user, pass);
    }

    @When("user adds item {string} to cart")
    public void userAddsItemToCart(String itemName) {
        homePage.addItem(itemName);
    }

    @When("user removes item {string} from cart")
    public void userRemovesItemFromCart(String itemName) {
        homePage.removeItem(itemName);
    }

    @Then("user should see {string} message")
    public void userShouldSeeMessage(String expectedMsg) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        String actualMsg = driver.findElement(By.tagName("h1")).getText();
        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Then("item {string} should be in cart")
    public void itemShouldBeInCart(String itemName) {
        String xpath = String.format("//div[@class='inventory_item_name' and text()='%s']", itemName);
        boolean isPresent = driver.findElements(By.xpath(xpath)).size() > 0;
        Assert.assertFalse(isPresent, "Item should not be in inventory after adding to cart");
    }
}
