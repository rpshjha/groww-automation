package com.qa.core;

import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafariDriverInstance {

    private SafariDriverInstance() {
    }

    private static Logger logger = LoggerFactory.getLogger(SafariDriverInstance.class);

    public static SafariDriver createDriverUsingSafari() {
        logger.info("Opening the browser : Safari");

        return new SafariDriver();
    }
}
