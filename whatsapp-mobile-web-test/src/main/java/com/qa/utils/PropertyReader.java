package com.qa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyReader {

    private PropertyReader() {
    }

    private static Properties properties = null;
    private static final String FILE_NAME = "application.properties";
    private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);

    static {
        initProperty();
    }

    private static void initProperty() {
        try {
            try (InputStream is = PropertyReader.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
                assert is != null;
                try (Reader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    properties = new Properties();
                    properties.load(bufferedReader);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("file not found {}", FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name key
     * @return String
     */
    public static String getProperty(String name) {
        return properties.getProperty(name);
    }
}
