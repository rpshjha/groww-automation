package com.qa.test.web;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.qa.core.DriverInstance;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected ExtentReports extentReports;

    @BeforeEach
    void setup() {
        DriverInstance.initializeDriver("web");
    }

    @AfterEach
    void tearDown() {
        DriverInstance.killDriver();
    }

    @BeforeAll
    void oneTimeSetup() {
        extentReports = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(
                "test-output/extent_report/qa_coding_challenge_report_web.html");
        extentReports.attachReporter(spark);
    }

    @AfterAll
    void oneTimeTearDown() {
        extentReports.flush();
    }

}
