package com.ddsolutions.stream.utility;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    String result = "";

    public static String getPropValues(String propertyKey){

        try {
            Properties prop = new Properties();
            String propFileName = "application.properties";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            // get the property value and print it out
            return prop.getProperty(propertyKey);
        } catch (Exception e) {
            return null;
        }
    }
}
