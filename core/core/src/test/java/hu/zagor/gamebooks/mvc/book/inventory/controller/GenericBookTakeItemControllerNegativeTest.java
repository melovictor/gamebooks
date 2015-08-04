package hu.zagor.gamebooks.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidGatheredItemException;
import hu.zagor.gamebooks.player.PlayerUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
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
    private GenericBookTakeItemController underTest;
    private IMocksControl mockControl;
    private BookInformations info;
    private HttpServletRequest request;
    private HttpSession session;
    private BeanFactory beanFactory;
    private HttpSessionWrapper sessionWrapper;
    private GatheredLostItem glItem;
    private PlayerUser playerUser;
    private Paragraph paragraph;
    private BookContentInitializer contentInitializer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        sessionWrapper = mockControl.createMock(HttpSessionWrapper.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        playerUser = mockControl.createMock(PlayerUser.class);
        paragraph = mockControl.createMock(Paragraph.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);

        info = new BookInformations(1L);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new GenericBookTakeItemController();
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "info", info);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, null, AMOUNT);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, "", AMOUNT);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, ITEM_ID, -1);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemTakeWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, ITEM_ID, 0);
        // THEN throws exception
    }

    @Test(expectedExceptions = InvalidGatheredItemException.class)
    public void testHandleItemTakeWhenIsNotValidShouldThrowException() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0)).andReturn(glItem);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expectLastCall().andThrow(new InvalidGatheredItemException(ITEM_ID, PARAGRAPH_ID));
        mockControl.replay();
        // WHEN
        underTest.handleItemTake(request, ITEM_ID, AMOUNT);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemReplaceWhenOldItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemReplace(request, "", "1002", 1);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemReplaceWhenNewItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemReplace(request, "1001", "", 1);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleItemReplaceWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleItemReplace(request, "1001", "1002", 0);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
