package hu.zagor.gamebooks.ff.ff.votv.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38BookTakeItemControllerBTest {
    @UnderTest private Ff38BookTakeItemController underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpServletRequest request;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Paragraph paragraph;
    @Mock private FfCharacter character;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private CommandView commandView;
    @Mock private List<Item> itemList;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(1L);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setCanEatEverywhere(true);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoHandleConsumeItemWhenActivatingLuckSpellWhileDoingLuckTestShouldAllowSpellActivation() {
        // GIVEN
        final String item = "4216";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffAttributeTest");
        character.changeLuck(3);
        expect(itemHandler.removeItem(character, item, 1)).andReturn(itemList);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenActivatingLuckSpellWhileFightingShouldNotAllowSpellActivation() {
        // GIVEN
        final String item = "4216";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffFightSingle");
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenActivatingHealingSpellWhileNotFightingShouldAllowSpellActivation() {
        // GIVEN
        final String item = "4218";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(20);
        character.changeStamina(10);
        expect(itemHandler.removeItem(character, item, 1)).andReturn(itemList);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenActivatingForcefieldSpellWhileNotFightingShouldDoNothing() {
        // GIVEN
        final String item = "4213";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenActivatingStrongHitSpellWhileFightingShouldAllowSpellActivation() {
        // GIVEN
        final String item = "4214";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffFightSingle");
        expect(itemHandler.addItem(character, "4013", 1)).andReturn(1);
        expect(itemHandler.removeItem(character, item, 1)).andReturn(itemList);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertEquals(returned, "#ffAttackButton button");
    }

    public void testDoHandleConsumeItemWhenActivatingJandorArrowSpellWhileFightingShouldAllowSpellActivation() {
        // GIVEN
        final String item = "4215";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffFightSingle");
        expect(itemHandler.addItem(character, "4014", 1)).andReturn(1);
        expect(itemHandler.removeItem(character, item, 1)).andReturn(itemList);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertEquals(returned, "#ffAttackButton button");
    }

    public void testDoHandleConsumeItemWhenActivatingBashSpellWhileFightingShouldAllowSpellActivation() {
        // GIVEN
        final String item = "4217";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffFightSingle");
        expect(itemHandler.addItem(character, "4015", 1)).andReturn(1);
        expect(itemHandler.removeItem(character, item, 1)).andReturn(itemList);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertEquals(returned, "#ffAttackButton button");
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
