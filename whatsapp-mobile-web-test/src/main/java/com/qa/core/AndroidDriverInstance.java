package com.qa.core;

import com.qa.utils.AppiumServer;
import com.testvagrant.commons.entities.DeviceDetails;
import com.testvagrant.mdb.android.Android;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static com.qa.utils.PropertyReader.getProperty;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.*;
import static io.appium.java_client.remote.AutomationName.ANDROID_UIAUTOMATOR2;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static io.appium.java_client.remote.MobilePlatform.ANDROID;

public class AndroidDriverInstance {

    private AndroidDriverInstance() {
    }

    private static final Logger logger = LoggerFactory.getLogger(AndroidDriverInstance.class);

    /**
     * creates android driver
     *
     * @return AndroidDriver
     */
    public static AndroidDriver createAndroidDriver() {
        logger.info("creating driver : Android");

        DeviceDetails deviceDetails;
        try {
            deviceDetails = new Android().getDevices().stream().findFirst().get();
        } catch (Exception e) {
            throw new RuntimeException("No android devices found");
        }
        AndroidDriver androidDriver;

        DesiredCapabilities cap = setAndroidCapability(deviceDetails);
        URL appiumServiceUrl = AppiumServer.startServer().getUrl();

        logger.info("initializing android driver");
        androidDriver = new AndroidDriver(appiumServiceUrl, cap);
        logger.info("android driver initialized");

        return androidDriver;
    }

    /**
     * set desired capability for android
     *
     * @return DesiredCapabilities
     */
    private static DesiredCapabilities setAndroidCapability(DeviceDetails deviceDetails) {

        DesiredCapabilities cap = new DesiredCapabilities();
        int newCommandTimeout = Integer.parseInt(getProperty("newCommandTimeout"));

        if (deviceDetails == null) {
            cap.setCapability(AVD, getProperty("nameOfAVD"));
        } else {
            cap.setCapability(UDID, deviceDetails.getUdid());
            cap.setCapability(DEVICE_NAME, deviceDetails.getDeviceName());
            cap.setCapability(PLATFORM_VERSION, deviceDetails.getOsVersion());
        }

        cap.setCapability(PLATFORM_NAME, ANDROID);
        cap.setCapability(APP_PACKAGE, getProperty("appPackage"));
        cap.setCapability(APP_ACTIVITY, getProperty("appActivity"));
        cap.setCapability(NEW_COMMAND_TIMEOUT, newCommandTimeout);
        cap.setCapability(AUTOMATION_NAME, ANDROID_UIAUTOMATOR2);
        cap.setCapability(AUTO_GRANT_PERMISSIONS, true);
        cap.setCapability(NO_RESET, true);

        return cap;
    }
}
