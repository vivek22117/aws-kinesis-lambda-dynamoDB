package com.ddsolutions.stream.utility;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    private static PropertyLoader propertyLoader = null;

    private static final String ENV = "environment";
    private static final String SUFFIX = ".properties";
    private static final String PREFIX = "/application";

    private PropertyLoader() {
    }

    public static PropertyLoader getInstance() {
        if (propertyLoader == null) {
            synchronized (PropertyLoader.class) {
                if (propertyLoader == null) {
                    propertyLoader = new PropertyLoader();
                }
            }
        }
        return propertyLoader;
    }

    public String getPropValues(String propertyKey) {
        String propFileName = null;

        try {
            String env = System.getenv(ENV);
            if (env != null) {
                propFileName = "-" + env;
                propFileName = PREFIX + propFileName + SUFFIX;
            }
            Properties prop = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            return prop.getProperty(propertyKey);
        } catch (Exception e) {
            return null;
        }
    }
}
