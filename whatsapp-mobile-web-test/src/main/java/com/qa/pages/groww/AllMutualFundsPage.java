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

public class AllMutualFundsPage extends Helper {

    private static final Logger logger = LoggerFactory.getLogger(AllMutualFundsPage.class);

    public AllMutualFundsPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        logger.info("navigating to website");
        this.driver.get("https://groww.in/mutual-funds/filter");
        waitForPageToLoad();
    }

    public boolean isAt() {
        logger.info("checking if the user is on mutual funds page");
        waitForPageToLoad();
        return getWait().until(d -> this.driver.getTitle().contains("Mutual Funds Screener: Find All Mutual Funds at one Place to invest on Groww"));
    }

    private String getFilterTitleLocator(String filterTitle) {
        return "//h3[contains(@class,'ac11Title')]/child::div/div[text()='" + filterTitle + "']";
    }

    public void filterMutualFundsByCategory(String category) {
        logger.info("applying filter by category {}", category);
        By xpath = By.xpath(getFilterTitleLocator(FilterTitle.CATEGORY) + "/ancestor::div[@class='cur-po ']/child::div/div[contains(@class, 'rah-static')]//div[@class='c11CLabel']/span[text()='" + category + "']");
        this.waitTillElementIsDisplayed(xpath).click();
        waitForPageToLoad();
    }


    public void filterMutualFundsByFundSize(String fundSize) {
        logger.info("applying filter by fund size {}", fundSize);
        By xpath = By.xpath(getFilterTitleLocator(FilterTitle.FUND_SIZE) + "/ancestor::div[@class='cur-po ']/child::div/div[contains(@class, 'rah-static')]//div[@class='c11CLabel'][text()='" + fundSize + "']");
        this.waitTillElementIsDisplayed(xpath).click();
        waitForPageToLoad();
    }

    public void filterMutualFundsByRisk(String risk) {
        logger.info("applying risk filter {}", risk);
        By xpath = By.xpath(getFilterTitleLocator(FilterTitle.RISK) + "/ancestor::div[@class='cur-po ']/child::div/div[contains(@class, 'rah-static')]//div[@class='c11CLabel'][text()='" + risk + "']");
        this.waitTillElementIsDisplayed(xpath).click();
        waitForPageToLoad();
    }

    public void sortBy(String sort) {
        logger.info("applying sorting by {}", sort);
        this.waitTillElementIsDisplayed(By.cssSelector("div.select11Box div")).click();
        this.waitTillElementIsDisplayed(By.xpath("//div[@class='c11CLabel'][text()='" + sort + "']")).click();
    }

    public void getBuyEstimateForMutualFundHoldings(int numberOfMF) {
        StringBuilder builder = new StringBuilder();
        String xpathMF = "//div[contains(@class,'ResultSec')]/descendant::tbody/tr/a/td[2]/div[contains(@class,'f22SchemeName')]/div[1]";

        try {
            for (int i = 0; i < numberOfMF; i++) {
                try {
                    WebElement element = this.waitTillAllElementsArePresent(By.xpath(xpathMF)).get(i);
                    String textMF = element.getText();

                    String parentWindowHandle = driver.getWindowHandle();
                    logger.info("clicking on {}", textMF);
                    this.getWait().ignoring(StaleElementReferenceException.class)
                                    .until(ExpectedConditions.elementToBeClickable(element)).click();
                    switchToWindow(textMF);

                    builder.append(textMF);
                    builder.append(",");
                    builder.append(" ");
                    builder.append('\n');

                    builder.append(getBuyEstimate());

                    driver.close();
                    driver.switchTo().window(parentWindowHandle);
                } catch (StaleElementReferenceException | TimeoutException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            FileUtils fileUtils = new FileUtils();
            fileUtils.createCSV(builder, "groww_mutual_fund");
        }
    }

    private void clickOnSeeAllUnderHoldings() {
        logger.info("clicking on See All");
        this.waitTillElementIsDisplayed(By.xpath("//div[text()='See All']")).click();
    }

    public StringBuilder getBuyEstimate() {
        clickOnSeeAllUnderHoldings();
        By xpathHoldings = By.xpath("//table[contains(@class,'holdings')]/tbody/tr/td[1]/a");
        List<String> listOfHoldings = this.waitTillAllElementsArePresent(xpathHoldings).stream().map(WebElement::getText).collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();

        for (String holdingsName : listOfHoldings) {
            String xpath = "//a/div[text()='" + holdingsName + "']";
            WebElement holdings = this.waitTillElementIsDisplayed(By.xpath(xpath));
            String text = holdings.getText();

            logger.info("clicking on holdings {}", text);
            holdings.click();
            waitForPageToLoad();

            String buyPercent = getBuyPercent();
            logger.info("Buy percent of {} is {}", text, buyPercent);

            String buyOrderQuantity = getMarketDepthPercentage();
            logger.info("Buy order and sell order of {} is {}", text, buyOrderQuantity);

            String nearTo52WLowPercentage = getNearTo52WLowPercentage();
            logger.info("nearTo52WLowPercentage of {} is {}", text, nearTo52WLowPercentage);

            logger.info("navigating back to previous page");
            this.driver.navigate().back();
            clickOnSeeAllUnderHoldings();
            waitForPageToLoad();
            builder.append(text);
            builder.append(",");
            builder.append(buyPercent);
            builder.append(",");
            builder.append(buyOrderQuantity);
            builder.append(",");
            builder.append(nearTo52WLowPercentage);
            builder.append('\n');
        }

        return builder;
    }

    public String getBuyPercent() {
        try {
            By xpathOfBuyPercent = By.xpath("//h2[text()='Analyst Estimates']/following::div[text()='Buy']/following-sibling::div[2]");
//            By xpathOfBuyPercent = By.xpath("//h2[text()='Analyst Estimates']/following::div[contains(@class,'clrSubText')][1]/div[text()='Buy']/following-sibling::div[2]");
            return this.getWait()
                    .withTimeout(Duration.ofSeconds(4))
                    .pollingEvery(Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfElementLocated(xpathOfBuyPercent))
                    .getText();
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error(e.getMessage());
            logger.error("buy percent not displayed, returning 0");
            return "0";
        }
    }

    public int getBuyPercentNoOfAnalyst() {
        try {
            By xpathOfBuyPercent = By.xpath("//h2[text()='Analyst Estimates']/following::div[contains(@class,'clrSubText')][4]");
            String text = this.getWait()
                    .withTimeout(Duration.ofSeconds(4))
                    .pollingEvery(Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfElementLocated(xpathOfBuyPercent))
                    .getText();
            return Integer.parseInt(text.replaceAll("[^\\d]", ""));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error(e.getMessage());
            logger.error("no of analyst not displayed, returning 0");
            return 0;
        }
    }

    public String getNearTo52WLowPercentage() {
        By xpath = By.xpath("//div[text()='52W Low']/parent::div/following-sibling::div[@class='pbar29RangesliderWrapper']/div/div[2]");

        String nearTo52WLowPercentage = "";
        try {
            nearTo52WLowPercentage = waitTillElementIsDisplayed(xpath, 5).getAttribute("style").replaceAll("[^0-9 \\.]", "").replaceAll("\\.\\d+$", "").trim();
        } catch (org.openqa.selenium.TimeoutException e) {
        }

        if (nearTo52WLowPercentage.isEmpty())
            return "0";

        return nearTo52WLowPercentage;
    }

    public String getMarketDepthPercentage() {
        By xpath = By.xpath("//div[contains(@class,'marketDepthUI_positionBar')]/div/div");

        waitForPageToLoad();
        String getBuyOrderQuantity = "";
        try {
            getBuyOrderQuantity = waitTillAllElementsArePresent(xpath, 5).get(0).getAttribute("style").replaceAll("[^0-9 \\.]", "").replaceAll("\\.\\d+$", "").trim();
        } catch (org.openqa.selenium.TimeoutException e) {
        }

        if (getBuyOrderQuantity.isEmpty())
            return "0";
        return getBuyOrderQuantity;
    }

}
