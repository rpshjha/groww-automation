package com.qa.core;

import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileDriverInstance {

    private static final Logger logger = LoggerFactory.getLogger(MobileDriverInstance.class);

    MobileDriverInstance() {
    }

    /**
     * Gets the mobile driver.
     *
     * @param <T>
     * @return AppiumDriver
     */
    <T extends AppiumDriver> T getMobileDriver() {

        String platformName;

        try {
            platformName = System.getProperty("platformName").toLowerCase();
        } catch (java.lang.NullPointerException nullPointerException) {
            logger.info("platformName not received , setting it to default");
            platformName = "android";
        }

        switch (platformName) {

            case "android":
                return (T) AndroidDriverInstance.createAndroidDriver();

            case "ios":
                //TODO currently ios implementation is not done
                return (T) IOSDriverInstance.createIOSDriver();

            default:
                logger.error("mobile platform can be either android or ios");
                throw new RuntimeException("mobile platform can be either android or ios");
        }

    }

}
