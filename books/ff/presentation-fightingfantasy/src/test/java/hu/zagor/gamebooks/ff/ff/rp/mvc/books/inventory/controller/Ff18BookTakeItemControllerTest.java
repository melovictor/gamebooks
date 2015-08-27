package hu.zagor.gamebooks.ff.ff.rp.mvc.books.inventory.controller;

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
 * Unit test for class {@link Ff18BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff18BookTakeItemControllerTest {

    private IMocksControl mockControl;
    private Ff18BookTakeItemController underTest;
    private HttpServletRequest request;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private BeanFactory beanFactory;
    private Paragraph paragraph;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private FfItem item;
    private Logger logger;

    private PlayerUser playerUser;
    private GatheredLostItem glItem;
    private FfCharacter character;
    private ItemInteractionRecorder itemInteractionRecorder;
    private BookContentInitializer contentInitializer;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff18BookTakeItemController();

        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        paragraph = mockControl.createMock(Paragraph.class);
        underTest.setBeanFactory(beanFactory);
        info = new FfBookInformations(1L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
        item = mockControl.createMock(FfItem.class);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "logger", logger);
        playerUser = mockControl.createMock(PlayerUser.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        character = mockControl.createMock(FfCharacter.class);
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoHandleItemTakeWhenTakingGenericItemShouldFallbackToDefaultBehaviour() {
        // GIVEN
        final String itemId = "3001";
        final int amount = 1;
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(99);
        expect(itemHandler.resolveItem(itemId)).andReturn(item);
        expect(item.getActions()).andReturn(1);
        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", itemId, amount, 0, false)).andReturn(glItem);
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(playerUser);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, itemId, amount)).andReturn(amount);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(itemId, amount);
        logger.debug("Took {} piece(s) of item {}.", amount, itemId);
        itemInteractionRecorder.recordItemTaking(wrapper, itemId);
        paragraph.setActions(98);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, amount);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testDoHandleItemTakeWhenTaking2001ShouldExecuteHealingAndCharging() {
        // GIVEN
        final String itemId = "2001";
        final int amount = 1;
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        character.changeStamina(4);
        attributeHandler.handleModification(character, "gold", -20);
        expect(itemHandler.addItem(character, "4001", 1)).andReturn(1);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, amount);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testDoHandleItemTakeWhenTaking2002ShouldExecuteHealingAndCharging() {
        // GIVEN
        final String itemId = "2002";
        final int amount = 1;
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        character.changeStamina(4);
        attributeHandler.handleModification(character, "gold", -50);
        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, itemId, amount);
        // THEN
        Assert.assertEquals(returned, 1);
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
