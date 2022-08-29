package com.qa.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafariDriverInstance {

    private SafariDriverInstance() {
    }

    private static Logger logger = LoggerFactory.getLogger(SafariDriverInstance.class);

    public static SafariDriver createDriverUsingSafari() {
        logger.info("Opening the browser : Safari");

        WebDriverManager.safaridriver().setup();
        return new SafariDriver();
    }
}
