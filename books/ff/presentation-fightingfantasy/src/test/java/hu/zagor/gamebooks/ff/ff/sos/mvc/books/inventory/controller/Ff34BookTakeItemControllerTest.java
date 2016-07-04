package hu.zagor.gamebooks.ff.ff.sos.mvc.books.inventory.controller;

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
 * Unit test for class {@link Ff34BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff34BookTakeItemControllerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff34BookTakeItemController underTest;
    @Mock private HttpServletRequest request;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Paragraph paragraph;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfItem item;
    @Mock private GatheredLostItem glItem;
    @Mock private FfCharacter character;
    @Mock private PlayerUser player;
    @Inject private BookContentInitializer contentInitializer;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;
    @Inject private Logger logger;
    @Mock private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(2);
        Whitebox.setInternalState(underTest, "info", info);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        characterHandler.setAttributeHandler(attributeHandler);
    }

    public void testDoHandleItemTakeWhenNormalItemShouldTakeIt() {
        // GIVEN
        final String provisionId = "2000";
        expectWrapper();
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        expect(itemHandler.resolveItem(provisionId)).andReturn(item);
        expect(item.getActions()).andReturn(1);

        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", provisionId, 1, 0, false)).andReturn(glItem);

        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, provisionId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(provisionId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, provisionId);
        itemInteractionRecorder.recordItemTaking(wrapper, provisionId);

        paragraph.setActions(9);

        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, provisionId, 1);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testDoHandleItemTakeWhenChainmailItemAndSmallInitialSkillShouldTakeAndEnhanceIt() {
        // GIVEN
        final String chainmailId = "3025";
        expectWrapper();
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        expect(itemHandler.resolveItem(chainmailId)).andReturn(item);
        expect(item.getActions()).andReturn(1);

        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", chainmailId, 1, 0, false)).andReturn(glItem);

        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, chainmailId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(chainmailId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, chainmailId);
        itemInteractionRecorder.recordItemTaking(wrapper, chainmailId);

        paragraph.setActions(9);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(10);
        expect(itemHandler.getItem(character, chainmailId)).andReturn(item);
        item.setInitialSkill(1);

        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, chainmailId, 1);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testDoHandleItemTakeWhenChainmailItemAndHighInitialSkillShouldNotEnhanceIt() {
        // GIVEN
        final String chainmailId = "3025";
        expectWrapper();
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        expect(itemHandler.resolveItem(chainmailId)).andReturn(item);
        expect(item.getActions()).andReturn(1);

        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", chainmailId, 1, 0, false)).andReturn(glItem);

        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, chainmailId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(chainmailId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, chainmailId);
        itemInteractionRecorder.recordItemTaking(wrapper, chainmailId);

        paragraph.setActions(9);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);

        mockControl.replay();
        // WHEN
        final int returned = underTest.doHandleItemTake(request, chainmailId, 1);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
