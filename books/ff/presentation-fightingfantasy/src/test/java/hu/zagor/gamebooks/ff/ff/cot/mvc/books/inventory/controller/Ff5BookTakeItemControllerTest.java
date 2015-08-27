package hu.zagor.gamebooks.ff.ff.cot.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff5BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff5BookTakeItemControllerTest {

    private IMocksControl mockControl;
    private Ff5BookTakeItemController underTest;
    private HttpServletRequest request;
    private HttpSession session;
    private BeanFactory beanFactory;
    private HttpSessionWrapper wrapper;
    private FfCharacter character;
    private Paragraph paragraph;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private FfItem item;
    private GatheredLostItem glItem;
    private PlayerUser player;
    private BookContentInitializer contentInitializer;
    private ItemInteractionRecorder itemInteractionRecorder;
    private Logger logger;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff5BookTakeItemController();
        request = mockControl.createMock(HttpServletRequest.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        session = mockControl.createMock(HttpSession.class);
        underTest.setBeanFactory(beanFactory);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(FfCharacter.class);
        paragraph = mockControl.createMock(Paragraph.class);
        info = new FfBookInformations(1L);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler = new FfCharacterHandler();
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
        item = mockControl.createMock(FfItem.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        player = mockControl.createMock(PlayerUser.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoHandleTakeItemWhenTakingColoredCandleButHaveNoGoldShouldDoNothing() {
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getGold()).andReturn(0);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, "3032", 1);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testDoHandleTakeItemWhenNotTakingColoredCandleShouldGoThroughNormalTakeItemProcess() {
        // GIVEN
        final String itemId = "3001";
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(1);
        expect(itemHandler.resolveItem(itemId)).andReturn(item);
        expect(item.getActions()).andReturn(2);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testDoHandleTakeItemWhenTakingColoredCandleAndHaveMoneyShouldGoThroughNormalTakeItemProcessAndLoseOneGoldPiece() {
        // GIVEN
        final String itemId = "3032";
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getGold()).andReturn(5);
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(99);
        expect(itemHandler.resolveItem(itemId)).andReturn(item);
        expect(item.getActions()).andReturn(1);
        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", itemId, 1, 0, false)).andReturn(glItem);
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, itemId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(itemId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, itemId);
        itemInteractionRecorder.recordItemTaking(wrapper, itemId);

        paragraph.setActions(98);
        expect(character.getGold()).andReturn(5);
        character.setGold(4);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    private void expectWrapper() {
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
