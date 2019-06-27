package com.rjsoft.component.codegenerator.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConstant {

    private static final String PROPERTIES_FILE = "db.properties";

    public static String getUrl() {
        return getProperty("url");
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }

    public static String getTableSchema() {
        return getProperty("tableSchema");
    }

    private static String getProperty(String key) {
        try {
            Properties properties = new Properties();
            InputStream in = DatabaseConstant.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            properties.load(in);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
