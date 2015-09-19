package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
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
 * Unit test for class {@link Ff1BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff1BookTakeItemControllerTest {

    private IMocksControl mockControl;
    private Ff1BookTakeItemController underTest;
    private HttpServletRequest request;
    private HttpSession session;
    private BeanFactory beanFactory;
    private HttpSessionWrapper wrapper;
    private Paragraph paragraph;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private FfItem item;
    private GatheredLostItem glItem;
    private PlayerUser player;
    private BookContentInitializer contentInitializer;
    private FfCharacter character;
    private Logger logger;
    private ItemInteractionRecorder itemInteractionRecorder;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff1BookTakeItemController();
        request = mockControl.createMock(HttpServletRequest.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest.setBeanFactory(beanFactory);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        paragraph = mockControl.createMock(Paragraph.class);
        info = new FfBookInformations(9L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        item = mockControl.createMock(FfItem.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        player = mockControl.createMock(PlayerUser.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        Whitebox.setInternalState(underTest, "info", info);
        character = mockControl.createMock(FfCharacter.class);
        logger = mockControl.createMock(Logger.class);
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        session = mockControl.createMock(HttpSession.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    public void testDoHandleItemTakeWhenTakingGenericItemShouldExecuteSuper() {
        // GIVEN
        getWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        final String itemId = "3001";
        expect(itemHandler.resolveItem(itemId)).andReturn(item);
        expect(item.getActions()).andReturn(1);

        getWrapper();
        expect(beanFactory.getBean("gatheredLostItem", itemId, 1, 0, false)).andReturn(glItem);
        getWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, itemId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(itemId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, itemId);
        itemInteractionRecorder.recordItemTaking(wrapper, itemId);

        paragraph.setActions(9);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testDoHandleItemTakeWhenTakingBlueCandleAndHaveEnoughMoneyShouldExecuteSuperAndDeductPrice() {
        // GIVEN
        final String itemId = "3016";
        getWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getGold()).andReturn(35);

        getWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        expect(itemHandler.resolveItem(itemId)).andReturn(item);
        expect(item.getActions()).andReturn(1);

        getWrapper();
        expect(beanFactory.getBean("gatheredLostItem", itemId, 1, 0, false)).andReturn(glItem);
        getWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, itemId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(itemId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, itemId);
        itemInteractionRecorder.recordItemTaking(wrapper, itemId);

        paragraph.setActions(9);

        attributeHandler.handleModification(character, "gold", -20);

        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testDoHandleItemTakeWhenTakingBlueCandleButDoesNotHaveEnoughMoneyShouldDoNothing() {
        // GIVEN
        final String itemId = "3016";
        getWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getGold()).andReturn(15);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    private void getWrapper() {
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
    }

}
