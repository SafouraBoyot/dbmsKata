package infrastructure.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {

    public static Properties getProperties(String filePath) {

        Properties properties = new Properties();
        InputStream input = null;


        try {
            input = LoadProperties.class.getClassLoader().getResourceAsStream(filePath);
            properties.load(input);
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
