package com.qa.core;

import com.qa.utils.AppiumServer;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static com.qa.utils.PropertyReader.getProperty;
import static io.appium.java_client.remote.AutomationName.IOS_XCUI_TEST;
import static io.appium.java_client.remote.IOSMobileCapabilityType.BUNDLE_ID;
import static io.appium.java_client.remote.IOSMobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static io.appium.java_client.remote.MobilePlatform.IOS;

public class IOSDriverInstance {

    private IOSDriverInstance() {
    }

    private static final Logger logger = LoggerFactory.getLogger(IOSDriverInstance.class);

    /**
     * creates ios driver
     *
     * @return IOSDriver
     */
    public static IOSDriver createIOSDriver() {
        logger.info("creating driver : IOS");

        IOSDriver iosDriver;

        DesiredCapabilities cap = setIOSCapability();
        URL appiumServiceUrl = AppiumServer.startServer().getUrl();

        logger.info("initializing ios driver");
        iosDriver = new IOSDriver(appiumServiceUrl, cap);
        logger.info("ios driver initialized");

        return iosDriver;
    }


    /**
     * set desired capability for ios
     *
     * @return DesiredCapabilities
     */
    private static DesiredCapabilities setIOSCapability() {

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(PLATFORM_NAME, IOS);
        cap.setCapability(PLATFORM_VERSION, "15.5");
        cap.setCapability(DEVICE_NAME, "iPhone 13");
        cap.setCapability(AUTOMATION_NAME, IOS_XCUI_TEST);
        cap.setCapability(BUNDLE_ID, getProperty("bundleId"));

        return cap;
    }
}
