package com.qa.pages.groww;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.qa.core.DriverInstance.getDriver;
import static com.qa.utils.PropertyReader.getProperty;

public class Helper {

    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    protected final WebDriver driver;
    private final WebDriverWait wait;
    private static final long TIMEOUT = Long.parseLong(getProperty("timeout"));


    public Helper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(TIMEOUT));
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public WebElement waitTillElementIsClickable(By locator) {
        logger.info("waiting for {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public List<WebElement> waitTillAllElementsArePresent(By locator) {
        logger.info("waiting for {}", locator);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> waitTillAllElementsArePresent(By locator, long seconds) {
        logger.info("waiting for {}", locator);
        return wait.withTimeout(Duration.ofSeconds(seconds)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public WebElement waitTillElementIsDisplayed(By locator) {
        logger.info("waiting for {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitTillElementIsDisplayed(By locator, long seconds) {
        logger.info("waiting for {}", locator);
        return wait
                .withTimeout(Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForPageToLoad() {
        logger.info("waiting for page to load");
        wait.until((ExpectedCondition<Boolean>) wdriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public void takeScreenshotAsFile(String fileName) {
        String nFileName = getProperty("screenshot.dir") + File.separator + fileName + ".png";
        logger.info("saving screenshot for {}", fileName);

        TakesScreenshot takesScreenshot = ((TakesScreenshot) getDriver());
        File src = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File dest = new File(System.getProperty("user.dir") + File.separator + nFileName);
        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] takeScreenshotAsBytes() {
        logger.info("saving screenshot");
        TakesScreenshot takesScreenshot = ((TakesScreenshot) getDriver());
        return takesScreenshot.getScreenshotAs(OutputType.BYTES);
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scroll() {
        logger.info("scrolling");
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1500)", "");
    }

    public void sendHumanKeys(By locator, String text) throws NoSuchAlgorithmException {
        Random rand = SecureRandom.getInstanceStrong();
        for (int i = 0; i < text.length(); i++) {
            waitForSeconds((int) (rand.nextGaussian() * 15 + 100));
            String s = String.valueOf(text.charAt(i));
            waitTillElementIsDisplayed(locator).sendKeys(s);
        }
    }

    private void waitForSeconds(int sec) {
        try {
            Thread.sleep((long) 1000 * sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomPhone() {
        return "74" + ThreadLocalRandom.current().nextInt(10000000, 99999999);
    }

    public void clickThruJS(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void switchToWindow(String title) {
        boolean foundWindow = false;
        String parentWindowHandle = driver.getWindowHandle();

        for (String handle : driver.getWindowHandles()) {
            if (driver.switchTo().window(handle).getTitle().contains(title)) {
                System.out.println("Switched to window with title: " + title);
                foundWindow = true;
                break;
            }
        }
        if (!foundWindow) {
            System.out.println("Couldn't find the window with title -> " + title + "\nSwitching to parent window.");
            driver.switchTo().window(parentWindowHandle);
        }
    }

    public List<String> getText(List<WebElement> elementList) {
        List<String> elStringList = new ArrayList<>();

        elementList.forEach(element -> elStringList.add(element.getText()));

        return elStringList;
    }

    public List<String> getAttribute(List<WebElement> elementList, String attr) {
        List<String> elStringList = new ArrayList<>();

        elementList.forEach(element -> elStringList.add(element.getAttribute(attr)));

        return elStringList;
    }

}
