package com.restfullReads.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private final Properties properties = new Properties();

    public ConfigLoader(String filename) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("Property file not found: " + filename);
            }
            properties.load(input);

            System.out.println(properties);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }
}
