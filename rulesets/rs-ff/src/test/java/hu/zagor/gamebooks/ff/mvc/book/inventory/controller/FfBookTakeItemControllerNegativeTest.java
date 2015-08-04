package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookTakeItemControllerNegativeTest {

    private FfBookTakeItemController underTest;
    private IMocksControl mockControl;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FfBookTakeItemController();
        request = mockControl.createMock(HttpServletRequest.class);
        response = mockControl.createMock(HttpServletResponse.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemStateChangeWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemStateChange(request, null, true);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemStateChangeWhenItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemStateChange(request, "", true);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleConsumeItemWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleConsumeItemWhenItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleMarketBuyWhenItemIdIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleMarketBuy(request, response, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleMarketBuyWhenItemIdEmptyNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleMarketBuy(request, response, "");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleMarketSellWhenItemIdIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleMarketSell(request, response, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleMarketSellWhenItemIdEmptyNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleMarketSell(request, response, "");
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
