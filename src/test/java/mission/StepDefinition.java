package mission;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class StepDefinition extends BasePage {

    // ================= UI SUPPORT =================
    HomePage homePage;

    // ================= API SUPPORT (RestAssured) =================
    private Response lastResponse;
    private List<Integer> allUserIds = new ArrayList<>();

    // ================= UI STEP DEFINITIONS (SAUCEDEMO) =================

    @Given("user is on the login page")
    public void userIsOnLoginPage() {
        homePage = new HomePage(driver);
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Swag Labs"), "Not on login page");
    }

    @Given("I am on the home page")
    public void i_am_on_the_home_page() {
        homePage = new HomePage(driver);
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Swag Labs"), "Not on Swag Labs home page");
    }

    @Given("I login in with the following details")
    public void i_login_in_with_the_following_details(DataTable table) {
        homePage = new HomePage(driver);
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        String user = rows.get(0).get("userName");
        String pass = rows.get(0).get("Password");
        homePage.login(user, pass);
    }

    @Given("I add the following items to the basket")
    public void i_add_the_following_items_to_the_basket(DataTable table) {
        homePage = new HomePage(driver);
        List<String> items = table.asList();
        for (String item : items) {
            homePage.addItem(item);
        }
    }

    @Given("I  should see {int} items added to the shopping cart")
    public void i_should_see_items_added_to_the_shopping_cart(int expected) {
        List<WebElement> badges = driver.findElements(By.cssSelector(".shopping_cart_badge"));
        int actual = badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
        Assert.assertEquals(actual, expected, "Shopping cart badge count mismatch");
    }

    @Given("I click on the shopping cart")
    public void i_click_on_the_shopping_cart() {
        driver.findElement(By.id("shopping_cart_container")).click();
    }

    @Given("I verify that the QTY count for each item should be 1")
    public void i_verify_that_the_qty_count_for_each_item_should_be_1() {
        List<WebElement> qtys = driver.findElements(By.cssSelector(".cart_quantity"));
        for (WebElement q : qtys) {
            Assert.assertEquals(q.getText().trim(), "1", "Cart quantity is not 1");
        }
    }

    @Given("I remove the following item:")
    public void i_remove_the_following_item(DataTable table) throws InterruptedException {
        homePage = new HomePage(driver);
        String itemName = table.asList().get(0);
        homePage.removeItem(itemName);
        Thread.sleep(1000); 
    }

    @Given("I  should see {int} items in the cart page")
    public void i_should_see_items_in_the_cart_page(int expected) {
        int actual = driver.findElements(By.cssSelector(".cart_item")).size();
        System.out.println("Cart items after remove = " + actual);
        
    }

    @Given("I accept the change password popup if present")
    public void i_accept_the_change_password_popup_if_present() {
        try {
            new org.openqa.selenium.interactions.Actions(driver)
                    .sendKeys(org.openqa.selenium.Keys.ESCAPE)
                    .perform();

            java.awt.Robot robot = new java.awt.Robot();
            robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);

            System.out.println("Popup dismissed using Robot and Actions.");
        } catch (Exception e) {
            System.out.println("No popup to dismiss or Robot failed.");
        }
    }

    @Given("I click on the CHECKOUT button")
    public void i_click_on_the_checkout_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement checkoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("checkout"))
        );

        
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", checkoutBtn);

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", checkoutBtn);

        
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        System.out.println("After checkout click URL: " + driver.getCurrentUrl());
    }


    @Given("I type {string} for First Name")
    public void i_type_for_first_name(String first) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement firstNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))
        );
        firstNameField.clear();
        firstNameField.sendKeys(first);
    }

    @Given("I type {string} for Last Name")
    public void i_type_for_last_name(String last) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement lastNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))
        );
        lastNameField.clear();
        lastNameField.sendKeys(last);
        Thread.sleep(1000); 
    }

    @Given("^I type \"([^\"]*)\" for ZIP.*Postal Code$")
    public void i_type_for_zip_postal_code(String zip) throws InterruptedException {
        WebElement zipField = driver.findElement(By.id("postal-code"));
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;

        
        js.executeScript("arguments[0].value='" + zip + "';", zipField);
        
        
        zipField.click();
        zipField.sendKeys(org.openqa.selenium.Keys.SPACE);
        zipField.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        
        
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", zipField);
        
        System.out.println("ZIP code entered and validated: " + zip);
        Thread.sleep(500); 
    }

    @When("I click on the CONTINUE button")
    public void i_click_on_the_continue_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement continueBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("continue"))
        );
        continueBtn.click();
    }

    @Then("Item total will be equal to the total of items on the list")
    public void item_total_will_be_equal_to_the_total_of_items_on_the_list() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));

        
        WebElement subtotalLabel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".summary_subtotal_label")
                )
        );

        
        List<WebElement> prices = driver.findElements(By.cssSelector(".inventory_item_price"));
        double sum = 0.0;
        for (WebElement p : prices) {
            sum += Double.parseDouble(p.getText().replace("$", "").trim());
        }

       
        String labelText = subtotalLabel.getText(); 
        double shownTotal = Double.parseDouble(
                labelText.substring(labelText.indexOf("$") + 1)
        );

        Assert.assertEquals(shownTotal, sum, 0.01,
                "Item total not equal to sum of items");
    }


   
    @Then("a Tax rate of {int} % is applied to the total")
    public void a_tax_rate_of_is_applied_to_the_total(int taxPercent) {
        // 1. Get Subtotal and Tax from UI
        double subtotal = Double.parseDouble(driver.findElement(By.className("summary_subtotal_label"))
                          .getText().replaceAll("[^0-9.]", ""));
        double actualTax = Double.parseDouble(driver.findElement(By.className("summary_tax_label"))
                          .getText().replaceAll("[^0-9.]", ""));

        // 2. Calculate Expected Tax
        double expectedTax = Math.round((subtotal * (taxPercent / 100.0)) * 100.0) / 100.0;

        // 3. Validation for Allure
        org.testng.Assert.assertEquals(actualTax, expectedTax, 
            String.format("Tax Calculation Error! Based on %d%% rate, expected: $%.2f, but UI showed: $%.2f", 
            taxPercent, expectedTax, actualTax));
    }

    // ================= API STEP DEFINITIONS (REQRES with RestAssured) =================
    @Given("I get the default list of users for on 1st page")
    public void i_get_the_default_list_of_users_for_on_1st_page() {
        lastResponse = ReqresClient.get("/users?page=1");
        
        Assert.assertNotNull(lastResponse, "No response from Reqres.");
        Assert.assertEquals(lastResponse.getStatusCode(), 200, 
            "Failed to get users. Check API Key. Status: " + lastResponse.getStatusCode());
    }

    @When("I get the list of all users within every page")
    public void i_get_the_list_of_all_users_within_every_page() {
        if (lastResponse == null) return;

        JsonPath json = lastResponse.jsonPath();
        int totalPages = json.getInt("total_pages");
        allUserIds.clear();

        for (int page = 1; page <= totalPages; page++) {
            Response pageResp = ReqresClient.get("/users?page=" + page);
            if (pageResp != null && pageResp.getStatusCode() == 200) {
                List<Integer> ids = pageResp.jsonPath().getList("data.id");
                allUserIds.addAll(ids);
            }
        }
    }

    @Then("I should see total users count equals the number of user ids")
    public void i_should_see_total_users_count_equals_the_number_of_user_ids() {
        int totalExpected = lastResponse.jsonPath().getInt("total");
        Assert.assertEquals(allUserIds.size(), totalExpected, "Total users mismatch across pages");
    }
    
 // ================= DYNAMIC SORTING =================

    @When("I sort products by {string}")
    public void i_sort_products_by(String sortOption) {
        homePage.sortProducts(sortOption);
    }

    @Then("the products should be displayed in ascending price order")
    public void the_products_should_be_displayed_in_ascending_price_order() {
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        
        
        List<Double> actualPrices = priceElements.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());

        
        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);

        Assert.assertEquals(actualPrices, sortedPrices, "Prices are NOT sorted correctly!");
    }

    // ================= NEGATIVE TESTING =================

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expectedError) {
        WebElement errorElement = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String actualError = errorElement.getText();
        Assert.assertTrue(actualError.contains(expectedError), 
            "Expected error: " + expectedError + " but got: " + actualError);
    }

    // ================= API SCENARIO: SINGLE USER / NOT FOUND =================

    @Given("I make a search for user {int}")
    public void i_make_a_search_for_user(Integer id) {
        lastResponse = ReqresClient.get("/users/" + id);
    }

    @Then("I should see the following user data")
    public void i_should_see_the_following_user_data(DataTable table) {
        Assert.assertEquals(lastResponse.getStatusCode(), 200, "User not found");
        
        Map<String, String> expected = table.asMaps(String.class, String.class).get(0);
        JsonPath json = lastResponse.jsonPath();

        Assert.assertEquals(json.getString("data.first_name"), expected.get("first_name"));
        Assert.assertEquals(json.getString("data.email"), expected.get("email"));
    }

    @Then("I receive error code {int} in response")
    public void i_receive_error_code_in_response(Integer code) {
        Assert.assertEquals(lastResponse.getStatusCode(), code.intValue(), "Status code mismatch");
    }

    // ================= API SCENARIO: CREATE USER =================

    @Given("I create a user with following {string} {string}")
    public void i_create_a_user_with_following(String name, String job) {
        // Create a Map for the body to let RestAssured handle JSON conversion
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("job", job);
        
        lastResponse = ReqresClient.post("/users", body);
    }

    @Then("response should contain the following data")
    public void response_should_contain_the_following_data(DataTable table) {
        
        Assert.assertEquals(lastResponse.getStatusCode(), 201, "User not created");

        JsonPath json = lastResponse.jsonPath();
       
        Assert.assertNotNull(json.getString("name"), "Name is missing in response");
        Assert.assertNotNull(json.getString("job"), "Job is missing in response");
        Assert.assertNotNull(json.getString("id"), "ID was not generated");
        
        System.out.println("Created User ID: " + json.getString("id"));
    }
 // =================  METHODS FOR PUT, PATCH, DELETE =================

    @Given("I update user {int} with name {string} and job {string} using PUT")
    public void i_update_user_with_name_and_job_using_put(Integer id, String name, String job) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("job", job);
        lastResponse = ReqresClient.put("/users/" + id, body);
    }

    @Then("the API status code should be {int}")
    public void the_api_status_code_should_be(int expectedCode) {
        Assert.assertNotNull(lastResponse, "Response was null");
        Assert.assertEquals(lastResponse.getStatusCode(), expectedCode, "Status code mismatch!");
    }

    @Then("response should contain the updated data")
    public void response_should_contain_the_updated_data(DataTable table) {
        Assert.assertNotNull(lastResponse, "Response was null");
        
        
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        Map<String, String> expected = data.get(0); 

        JsonPath json = lastResponse.jsonPath();
        
        Assert.assertEquals(json.getString("name"), expected.get("name"), "Name mismatch");
        Assert.assertEquals(json.getString("job"), expected.get("job"), "Job mismatch");
        Assert.assertNotNull(json.getString("updatedAt"), "updatedAt field missing");
    }

    @Given("I update user {int} with job {string} using PATCH")
    public void i_update_user_with_job_using_patch(Integer id, String job) {
        Map<String, String> body = new HashMap<>();
        body.put("job", job);
        lastResponse = ReqresClient.patch("/users/" + id, body);
    }

    @Then("response should contain {string} as {string}")
    public void response_should_contain_as(String key, String value) {
        Assert.assertNotNull(lastResponse, "PATCH Response is null");
        Assert.assertEquals(lastResponse.jsonPath().getString(key), value, "Field value mismatch");
    }

    @When("I delete user {int}")
    public void i_delete_user(Integer id) {
        lastResponse = ReqresClient.delete("/users/" + id);
    }
}