package com.qa.core;

import com.qa.utils.PropertyReader;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeDriverInstance {

    private ChromeDriverInstance() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ChromeDriverInstance.class);

    public static ChromeDriver createDriverUsingChrome() {
        logger.info("Opening the browser : Chrome");

        ChromeOptions chromeoptions = new ChromeOptions();
        chromeoptions.addArguments("--disable-notifications");
        if (Boolean.parseBoolean(PropertyReader.getProperty("headless"))) {
            logger.info("running the test in headless mode");
            chromeoptions.setHeadless(true);
        }
        return new ChromeDriver(chromeoptions);
    }
}
