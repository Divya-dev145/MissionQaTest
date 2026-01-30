package mission;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProp extends BasePage {

    static Properties prop;
    static FileInputStream input;
   
    public static final String propFilePath = System.getProperty("user.dir") + "/src/test/java/TestData/TestData.properties";

    public static String getProperty(String key) {
        prop = new Properties();
        try {
            input = new FileInputStream(propFilePath);
            prop.load(input);
            input.close();
        } catch (IOException e) {
            System.out.println("Could not find properties file at: " + propFilePath);
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}