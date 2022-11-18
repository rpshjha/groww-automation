package com.qa.pages.groww;

import com.qa.utils.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MarketsPage extends Helper {

    private static final Logger logger = LoggerFactory.getLogger(MarketsPage.class);

    public MarketsPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        logger.info("navigating to website");
        this.driver.get("https://groww.in/markets");
        waitForPageToLoad();
    }

    public boolean isAt() {
        logger.info("checking if the user is on market trends page");
        waitForPageToLoad();
        return getWait().until(d -> this.driver.getTitle().contains("Stock Market Trends Today - NSE & BSE Stocks Analysis"));
    }

    public void goToTab(String text) {
        this.waitTillAllElementsArePresent(By.cssSelector("div.tabs8Parent div")).stream()
                .filter(element ->
                        element.getText().contains(text)
                )
                .findFirst().ifPresent(WebElement::click);
    }

    public void filterBy(String text) {
        waitForPageToLoad();

        this.waitTillElementIsClickable(By.xpath("//div[contains(@class,'DropDownMain')]")).click();

        this.waitTillAllElementsArePresent(By.xpath("//div[contains(@class,'DropdownPara')]"))
                .stream().filter(element ->
                        element.getText().contains(text)
                )
                .findAny().ifPresentOrElse(WebElement::click, () -> System.out.println("No value stored in the Optional object"));
    }

    public List<String> getAllFundName() throws InterruptedException {
        waitForPageToLoad();
        By xpath = By.xpath("//a[contains(@class,'CompanyName')]");

        String prev = null;
        String next = null;
        List<WebElement> first = new ArrayList<>();
        List<WebElement> second = new ArrayList<>();
        while (true) {

            first = this.waitTillAllElementsArePresent(xpath);
            List<String> text = getText(first);
            prev = text.get(text.size() - 1);
            logger.info("current is " + prev);
            scroll();
            Thread.sleep(4000);
            waitForPageToLoad();
            second = this.waitTillAllElementsArePresent(xpath);
            List<String> text2 = getText(second);
            next = text2.get(text2.size() - 1);
            logger.info("next is " + next);

            if (prev.equals(next))
                break;
        }


        List<WebElement> element = this.getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(xpath));
        List<String> allFund = getText(element);
        WebElement element1 = element.get(element.size() - 1);
        logger.info("size is " + element.size());
        logger.info("size is " + element1.getText());
        return allFund;
    }

    public List<String> getAllFundLink() throws InterruptedException {
        waitForPageToLoad();
        By xpath = By.xpath("//a[contains(@class,'CompanyName')]");

        String prev = null;
        String next = null;
        List<WebElement> first = new ArrayList<>();
        List<WebElement> second = new ArrayList<>();
        while (true) {

            first = this.waitTillAllElementsArePresent(xpath);
            List<String> text = getText(first);
            prev = text.get(text.size() - 1);
            logger.info("current is " + prev);
            scroll();
            Thread.sleep(4000);
            waitForPageToLoad();
            second = this.waitTillAllElementsArePresent(xpath);
            List<String> text2 = getText(second);
            next = text2.get(text2.size() - 1);
            logger.info("next is " + next);

            if (prev.equals(next))
                break;
        }


        List<WebElement> element = this.getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(xpath));
        List<String> allFund = getAttribute(element, "href");
        WebElement element1 = element.get(element.size() - 1);
        logger.info("size is " + element.size());
        logger.info("size is " + element1.getText());
        return allFund;
    }

    public Object[] getBuyPercentFor(String text) {
        By xpath = By.xpath("//a[contains(@class,'CompanyName')][text()='" + text + "']");
        int percentage = 0;
        int buyEstimateNo = 0;
        try {
            this.getWait().until(ExpectedConditions.elementToBeClickable(xpath)).click();
            HomePage homePage = new HomePage(this.driver);
            percentage = Integer.parseInt(homePage.getBuyPercent().replaceAll("[^\\d]", ""));
            buyEstimateNo = homePage.getBuyPercentNoOfAnalyst();
            System.out.println("buy percent is " + percentage + " for mf " + text);
            driver.navigate().back();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return new Object[]{text, percentage, buyEstimateNo};
    }

    public Object[] getBuyPercentForStocksByNavigatingToLink(String link) {
        String stockName = link.split("\\/")[link.split("\\/").length -1];
        int percentage = 0;
        int buyEstimateNo = 0;
        try {
            this.driver.navigate().to(link);
            HomePage homePage = new HomePage(this.driver);
            percentage = Integer.parseInt(homePage.getBuyPercent().replaceAll("[^\\d]", ""));
            buyEstimateNo = homePage.getBuyPercentNoOfAnalyst();
            System.out.println("buy percent is " + percentage + " for stock  " + stockName);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new Object[]{stockName, link, percentage, buyEstimateNo};
    }

    public void getBuyPercentForAllStocks(String category, String marketTrends) throws InterruptedException {
        List<String> allFundName = getAllFundLink();

        StringBuilder builder = new StringBuilder();
        builder.append("stock name");
        builder.append(",");
        builder.append("link");
        builder.append(",");
        builder.append("buy percentage");
        builder.append(",");
        builder.append("buy estimate from analyst");
        builder.append('\n');

        try {
            allFundName.forEach(s -> {
                Object[] data = getBuyPercentForStocksByNavigatingToLink(s);
                if ((int) data[2] > 80 && (int) data[3] > 8) {
                    builder.append(data[0]);
                    builder.append(",");
                    builder.append(data[1]);
                    builder.append(",");
                    builder.append(data[2]);
                    builder.append(",");
                    builder.append(data[3]);
                    builder.append('\n');
                }
            });
        } finally {
            FileUtils fileUtils = new FileUtils();
            fileUtils.createCSV(builder, "groww_buy_percent_" + category + "_" + marketTrends + "_");
        }
    }


}
