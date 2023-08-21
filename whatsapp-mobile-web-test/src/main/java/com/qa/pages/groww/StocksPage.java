package com.qa.pages.groww;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StocksPage extends Helper {

    private static final Logger logger = LoggerFactory.getLogger(StocksPage.class);

    public StocksPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        logger.info("navigating to website");
        this.driver.get("https://groww.in/stocks");
        waitForPageToLoad();
    }

    public boolean isAt() {
        logger.info("checking if the user is on stocks trends page");
        waitForPageToLoad();
        return getWait().until(d -> this.driver.getTitle().contains("Stock Market LIVE - BSE, NSE & Nifty | Sensex Share Price, Indian Stock/Share Market & Stock Exchange | Groww"));
    }

    public void getStarted() {
        waitTillAllElementsArePresent(By.xpath("//span[text()='Get started']")).get(0).click();
    }
}
