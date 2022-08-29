package com.qa.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.appium.java_client.service.local.AppiumDriverLocalService.buildService;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.*;

public class AppiumServer {

    private static final Logger logger = LoggerFactory.getLogger(AppiumServer.class);
    private static final String IP = "127.0.0.1";
    private static final int PORT = 4723;

    private AppiumServer() {
    }

    private static final ThreadLocal<AppiumDriverLocalService> appiumService = new ThreadLocal<>();

    public static AppiumDriverLocalService startServer() {
        logger.info("starting appium server on " + IP + " and port " + PORT);
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();

        appiumServiceBuilder.withArgument(LOG_LEVEL, "error")
                .withArgument(SESSION_OVERRIDE)
                .withArgument(BASEPATH, "/wd/hub/")
                .usingPort(PORT)
                .withIPAddress(IP);

        appiumService.set(buildService(appiumServiceBuilder));
        appiumService.get().start();
        logger.info("appium server started");
        return appiumService.get();
    }

    public static void stopServer() {
        logger.info("stopping the appium server");
        if (appiumService.get() != null) {
            appiumService.get().stop();
            appiumService.remove();
        }
    }
}
