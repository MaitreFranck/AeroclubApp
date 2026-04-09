package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            System.err.println("Fichier config.properties non trouvé, passage en mode H2 par défaut.");
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}