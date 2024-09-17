package lesson_24;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {

    public static Properties getProperties(String pathToProperties) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(pathToProperties)) {
            prop.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(pathToProperties, ex);
        }
        return prop;
    }
}

