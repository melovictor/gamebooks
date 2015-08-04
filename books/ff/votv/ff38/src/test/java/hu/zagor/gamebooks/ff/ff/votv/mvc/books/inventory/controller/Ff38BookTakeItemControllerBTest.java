package hu.zagor.gamebooks.ff.ff.votv.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
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

    private Ff38BookTakeItemController underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private HttpServletRequest request;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private Paragraph paragraph;
    private FfCharacter character;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private FfAttributeHandler attributeHandler;
    private CommandView commandView;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff38BookTakeItemController();
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest.setBeanFactory(beanFactory);
        request = mockControl.createMock(HttpServletRequest.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        session = mockControl.createMock(HttpSession.class);
        paragraph = mockControl.createMock(Paragraph.class);
        character = mockControl.createMock(FfCharacter.class);
        info = new FfBookInformations(1L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setCanEatEverywhere(true);
        commandView = mockControl.createMock(CommandView.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testConstructor() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getClass();
        // THEN
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
        itemHandler.removeItem(character, item, 1);
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
        itemHandler.removeItem(character, item, 1);
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
        itemHandler.removeItem(character, item, 1);
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
        itemHandler.removeItem(character, item, 1);
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
        itemHandler.removeItem(character, item, 1);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertEquals(returned, "#ffAttackButton button");
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
