package hu.zagor.gamebooks.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidGatheredItemException;
import hu.zagor.gamebooks.mvc.book.inventory.domain.ReplaceItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemData;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookTakeItemControllerNegativeTest {

    private static final String ITEM_ID = "3001";
    private static final int AMOUNT = 5;
    private static final String PARAGRAPH_ID = "17";
    @UnderTest private GenericBookTakeItemController underTest;
    @MockControl private IMocksControl mockControl;
    private BookInformations info;
    @Mock private HttpServletRequest request;
    @Mock private HttpSession session;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper sessionWrapper;
    @Mock private GatheredLostItem glItem;
    @Mock private PlayerUser playerUser;
    @Mock private Paragraph paragraph;
    @Inject private BookContentInitializer contentInitializer;
    @Instance private TakeItemData takeData;
    @Instance private ReplaceItemData replaceData;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        takeData.setAmount(AMOUNT);
        takeData.setItemId(null);
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, takeData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenItemIdIsEmptyShouldThrowException() {
        // GIVEN
        takeData.setAmount(AMOUNT);
        takeData.setItemId("");
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, takeData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        takeData.setAmount(-1);
        takeData.setItemId(ITEM_ID);
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, takeData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        takeData.setAmount(0);
        takeData.setItemId(ITEM_ID);
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, takeData);
        // THEN throws exception
    }

    @Test(expectedExceptions = InvalidGatheredItemException.class)
    public void testHandleItemTakeWhenIsNotValidShouldThrowException() {
        // GIVEN
        takeData.setAmount(AMOUNT);
        takeData.setItemId(ITEM_ID);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expectLastCall().andThrow(new InvalidGatheredItemException(ITEM_ID, PARAGRAPH_ID));
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, takeData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemReplaceWhenOldItemIdIsEmptyShouldThrowException() {
        // GIVEN
        replaceData.setLoseId("");
        replaceData.setGatherId("1002");
        replaceData.setAmount(1);
        mockControl.replay();
        // WHEN
        underTest.handleItemReplace(request, replaceData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemReplaceWhenNewItemIdIsEmptyShouldThrowException() {
        // GIVEN
        replaceData.setLoseId("1001");
        replaceData.setGatherId("");
        replaceData.setAmount(1);
        mockControl.replay();
        // WHEN
        underTest.handleItemReplace(request, replaceData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemReplaceWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        replaceData.setLoseId("1001");
        replaceData.setGatherId("1002");
        replaceData.setAmount(0);
        mockControl.replay();
        // WHEN
        underTest.handleItemReplace(request, replaceData);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
