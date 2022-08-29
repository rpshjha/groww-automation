package com.qa.core;

import com.qa.utils.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirefoxDriverInstance {

    private FirefoxDriverInstance() {
    }

    private static final Logger logger = LoggerFactory.getLogger(FirefoxDriverInstance.class);

    public static FirefoxDriver createDriverUsingFirefox() {
        logger.info("Opening the browser : Firefox");

        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxoptions = new FirefoxOptions();
        if (Boolean.parseBoolean(PropertyReader.getProperty("headless"))) {
            logger.info("running the test in headless mode");
            firefoxoptions.setHeadless(true);
        }
        return new FirefoxDriver(firefoxoptions);
    }
}
