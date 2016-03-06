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
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff18BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff18BookTakeItemControllerTest {
    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff18BookTakeItemController underTest;
    @Mock private HttpServletRequest request;
    @Mock private HttpSessionWrapper wrapper;
    @Inject private BeanFactory beanFactory;
    @Mock private Paragraph paragraph;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfItem item;
    @Inject private Logger logger;
    @Mock private PlayerUser playerUser;
    @Mock private GatheredLostItem glItem;
    @Mock private FfCharacter character;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;
    @Inject private BookContentInitializer contentInitializer;
    @Mock private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(1L);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
