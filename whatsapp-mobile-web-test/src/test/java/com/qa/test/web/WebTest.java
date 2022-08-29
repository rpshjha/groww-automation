package com.qa.test.web;

import com.qa.pages.constant.Category;
import com.qa.pages.constant.FundSize;
import com.qa.pages.constant.Risk;
import com.qa.pages.constant.SortBy;
import com.qa.pages.groww.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.qa.core.DriverInstance.getDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebTest extends BaseTest {

    @Test
    @DisplayName("get buy estimate for stocks")
    void test() {

        HomePage homePage = new HomePage(getDriver());

        homePage.goTo();
        assertTrue(homePage.isAt(), "verify if page is loaded");

        homePage.goToMutualFundPage();

        homePage.filterMutualFundsByCategory(Category.EQUITY);
        homePage.filterMutualFundsByFundSize(FundSize.TEN_THOUSAND_CR);
        homePage.filterMutualFundsByRisk(Risk.HIGH);
        homePage.filterMutualFundsByRisk(Risk.VERY_HIGH);
        homePage.sortBy(SortBy.RETURNS_HIGH_TO_LOW);

        homePage.getBuyEstimateForMutualFundHoldings(6);
    }

}
