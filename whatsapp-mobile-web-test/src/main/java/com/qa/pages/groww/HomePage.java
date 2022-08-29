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

    public void goToMutualFundPage() {
        this.waitTillElementIsClickable(By.cssSelector("a#homeBox1[href='/mutual-funds']")).click();
        switchToWindow("Mutual Funds - Online Mutual Fund Investment, Best Mutual Funds India | Groww");
        logger.info("closing login popup window");
        this.waitTillElementIsClickable(By.cssSelector("span.rodal-close")).click();
        logger.info("clicking on explore all mutual funds");
        this.waitTillElementIsClickable(By.xpath("//span[text()='Explore all Mutual funds']")).click();
        waitForPageToLoad();
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
        this.waitTillElementIsDisplayed(By.xpath("//div[@class='c11CLabel'][text()='" + sort + "']"));
    }

    public void getBuyEstimateForMutualFundHoldings(int numberOfMF) {
        StringBuilder builder = new StringBuilder();
        String xpathMF = "//div[contains(@class,'ResultSec')]/descendant::tbody/tr/a/td[2]/div[contains(@class,'f22SchemeName')]/div[contains(@class,'clrText')]";

        try {
            for (int i = 0; i < numberOfMF; i++) {
                try {
                    WebElement element = this.waitTillAllElementsArePresent(By.xpath(xpathMF)).get(i);
                    String textMF = element.getText();

                    String parentWindowHandle = driver.getWindowHandle();
                    logger.info("clicking on {}", textMF);
                    element.click();
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

    private void clickOnSeeAll() {
        logger.info("clicking on See All");
        this.waitTillElementIsDisplayed(By.xpath("//div[text()='See All']")).click();
    }

    private StringBuilder getBuyEstimate() {
        clickOnSeeAll();
        By xpathHoldings = By.xpath("//table[contains(@class,'holdings')]/tbody/tr/td[1]/a");
        List<String> listOfHoldings = this.waitTillAllElementsArePresent(xpathHoldings).stream().map(WebElement::getText).collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();

        for (String holdingsName : listOfHoldings) {
            String xpath = "//a[text()='" + holdingsName + "']";
            WebElement holdings = this.waitTillElementIsDisplayed(By.xpath(xpath));
            String text = holdings.getText();

            logger.info("clicking on holdings {}", text);
            holdings.click();
            waitForPageToLoad();
            String buyPercent = getBuyPercent();
            logger.info("Buy percent of {} is {}", text, buyPercent);
            logger.info("navigating back to previous page");
            this.driver.navigate().back();
            clickOnSeeAll();
            waitForPageToLoad();
            builder.append(text);
            builder.append(",");
            builder.append(buyPercent);
            builder.append('\n');
        }

        return builder;
    }

    private String getBuyPercent() {
        try {
            By xpathOfBuyPercent = By.xpath("//h2[text()='Analyst Estimates']/following::div[contains(@class,'clrSubText')][1]/div[text()='Buy']/following-sibling::div[2]");
            return this.getWait()
                    .withTimeout(Duration.ofSeconds(4))
                    .pollingEvery(Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfElementLocated(xpathOfBuyPercent))
                    .getText();
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error("buy percent not displayed, returning 0");
            return "0";
        }
    }

}
