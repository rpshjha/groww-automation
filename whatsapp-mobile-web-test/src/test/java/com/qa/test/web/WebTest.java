package com.qa.test.web;

import com.qa.pages.constant.Category;
import com.qa.pages.constant.FundSize;
import com.qa.pages.constant.Risk;
import com.qa.pages.constant.SortBy;
import com.qa.pages.groww.HomePage;
import com.qa.pages.groww.MarketsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.qa.core.DriverInstance.getDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebTest extends BaseTest {

    @Test
    @DisplayName("get buy estimate for stocks")
    void test1() {

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

    @ParameterizedTest
//    @CsvSource({"NIFTY 100,Top gainers", "NIFTY 100,Top losers", "NIFTY 500,Top losers", "NIFTY Midcap 100,Top by volume", "NIFTY Midcap 100,52W low"})
//    @CsvSource({"NIFTY 100,52W low", "NIFTY 500,52W low", "NIFTY Midcap 100,52W low", "NIFTY Smallcap 100,52W low"})
    @CsvSource({"NIFTY 100,Top losers", "NIFTY 500,Top losers", "NIFTY Midcap 100,Top losers", "NIFTY Smallcap 100,Top losers"})
    @DisplayName("get buy estimate for stocks")
    void test2(String category, String marketTrends) throws InterruptedException {

        MarketsPage marketsPage = new MarketsPage(getDriver());

        marketsPage.goTo();
        assertTrue(marketsPage.isAt(), "verify if page is loaded");

        marketsPage.filterBy(category);
        marketsPage.goToTab(marketTrends);

        marketsPage.getBuyPercentForAllStocks(category, marketTrends);
    }

}
