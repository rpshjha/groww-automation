package com.qa.pages.groww;

import com.qa.pages.constant.FilterTitle;
import com.qa.utils.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends Helper {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        logger.info("navigating to website");
        this.driver.get("https://groww.in/");
        waitForPageToLoad();
    }

    public boolean isAt() {
        logger.info("checking if the user is on home page");
        waitForPageToLoad();
        return getWait().until(d -> this.driver.getTitle().contains("Groww - Online Demat, Trading and Direct Mutual Fund Investment in India"));
    }

}
