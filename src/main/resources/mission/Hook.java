package mission;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;

public class Hook extends BasePage {

    BrowserSetup browserSetup = new BrowserSetup();

    @Before("@ui")
    public void setUp() {
        browserSetup.selectBrowser();
        driver.manage().window().maximize();
        driver.get(LoadProp.getProperty("url"));
    }

    @After("@ui")
    public void tearDown(Scenario scenario) {
        try {
            if (driver != null) {

                // TAKE SCREENSHOT ALWAYS (for debugging) – change to scenario.isFailed() later
                try {
                    TakesScreenshot ts = (TakesScreenshot) driver;
                    File src = ts.getScreenshotAs(OutputType.FILE);

                    String folder = LoadProp.getProperty("ScreenshotLocation"); // target/screenshots/
                    if (!folder.endsWith("/") && !folder.endsWith("\\")) {
                        folder = folder + "/";
                    }
                    String fileName = scenario.getName()
                            .replaceAll("[^a-zA-Z0-9-_]", "_") + ".png";
                    File dest = new File(folder + fileName);
                    dest.getParentFile().mkdirs();
                    FileUtils.copyFile(src, dest);
                    System.out.println("Screenshot saved at: " + dest.getAbsolutePath());
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
