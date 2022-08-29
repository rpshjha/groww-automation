package com.qa.core;

import com.qa.utils.AppiumServer;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverInstance {

    private static final Logger logger = LoggerFactory.getLogger(DriverInstance.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverInstance() {
    }

    /**
     * @return WebDriver
     */
    public static void initializeDriver(final String platform) {
        if (platform == null) throw new RuntimeException("platform name can not be null.Enter as mobile or web");

        System.setProperty("platform", platform.toLowerCase());

        switch (platform.toLowerCase()) {

            case "web":
                WebDriverInstance webDriverInstance = new WebDriverInstance();
                driver.set(webDriverInstance.getBrowserDriver());
                break;

            case "mobile":
                MobileDriverInstance mobileDriverInstance = new MobileDriverInstance();
                driver.set(mobileDriverInstance.getMobileDriver());
                break;

            default:
                logger.error("platform name entered is not correct.Enter as mobile or web");
                throw new RuntimeException("platform name entered is not correct.Enter as mobile or web");
        }
    }

    /**
     * gets the driver
     *
     * @return Web driver
     */
    public static WebDriver getDriver() {
        if (driver.get() == null)
            throw new RuntimeException("webDriver or appiumDriver not initialized.please initialize the driver by calling initializeDriver(String platform) method");

        return driver.get();
    }


    /**
     * kills the driver
     */
    public static void killDriver() {
        logger.info("closing the driver");

        if (driver.get() != null) {
            getDriver().quit();
            AppiumServer.stopServer();
        }
        driver.remove();
    }

}

