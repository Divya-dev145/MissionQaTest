package mission;

import io.cucumber.java.After;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;   
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class Hook extends BasePage {

    BrowserSetup browserSetup = new BrowserSetup();

    @Before("@ui")
    public void setUp() {
        browserSetup.selectBrowser();
        driver.manage().window().maximize();
        driver.get(LoadProp.getProperty("url"));
    }

    
    @AfterStep("@ui")
    public void attachScreenshotAfterStep(Scenario scenario) {
        if (driver != null) {
            try {
                
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                
               
                InputStream screenshotStream = new ByteArrayInputStream(screenshot);
                Allure.addAttachment("Step Screenshot", screenshotStream);
                
            } catch (Exception e) {
                System.err.println("Failed to attach screenshot to Allure: " + e.getMessage());
            }
        }
    }

    @After("@ui")
    public void tearDown(Scenario scenario) {
        try {
            if (driver != null) {
                
                try {
                    TakesScreenshot ts = (TakesScreenshot) driver;
                    File src = ts.getScreenshotAs(OutputType.FILE);

                    String folder = LoadProp.getProperty("ScreenshotLocation");
                    if (folder == null || folder.trim().isEmpty()) {
                        folder = "target/screenshots/";
                    }
                    if (!folder.endsWith("/") && !folder.endsWith("\\")) {
                        folder = folder + "/";
                    }

                    String fileName = scenario.getName()
                            .replaceAll("[^a-zA-Z0-9-_]", "_") + "_FINAL.png";

                    File dest = new File(folder + fileName);
                    dest.getParentFile().mkdirs();
                    FileUtils.copyFile(src, dest);
                    System.out.println("Final state screenshot saved at: " + dest.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver = null;
        }
    }
}