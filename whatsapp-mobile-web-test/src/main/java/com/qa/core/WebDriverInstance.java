package com.qa.core;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.qa.utils.PropertyReader.getProperty;

public class WebDriverInstance {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverInstance.class);

    WebDriverInstance() {
    }

    /**
     * Gets the browser driver.
     *
     * @param <T> type of driver
     * @return WebDriver
     */
    <T extends WebDriver> T getBrowserDriver() {

        T driver;
        String browserName;

        try {
            browserName = System.getProperty("browserName").toLowerCase();
        } catch (java.lang.NullPointerException nullPointerException) {
            logger.error("browserName not received , setting it to default");
            browserName = getProperty("default.browser").toLowerCase();
        }

        switch (browserName) {

            case "firefox":
                driver = (T) FirefoxDriverInstance.createDriverUsingFirefox();
                break;
            case "chrome":
                driver = (T) ChromeDriverInstance.createDriverUsingChrome();
                break;
            case "safari":
                driver = (T) SafariDriverInstance.createDriverUsingSafari();
                break;

            default:
                logger.error("browserName can be either chrome, safari or firefox");
                throw new IllegalArgumentException(browserName);
        }

        if (Boolean.parseBoolean(getProperty("isWindowMax"))) driver.manage().window().maximize();

        return driver;
    }

}
