package hu.zagor.gamebooks.ff.ff.votv.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;

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
public class Ff38BookTakeItemControllerATest {

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
    private FfParagraphData paragraphData;
    private FfItem item;
    private FfAttributeHandler attributeHandler;
    private ItemInteractionRecorder itemInteractionRecorder;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff38BookTakeItemController();
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);
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
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        paragraphData = mockControl.createMock(FfParagraphData.class);
        item = mockControl.createMock(FfItem.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setCanEatEverywhere(true);
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

    public void testDoHandleConsumeItemWhenGhostIsInRoomShouldNotBeAbleToConsumeAnything() {
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("98");
        expect(wrapper.getCharacter()).andReturn(character);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, "2000");
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenCannotEatAnymoreWhileHeydrichRegeneratesShouldNotBeAbleToConsumeAnything() {
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("63");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "4021")).andReturn(true);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, "2000");
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenStillCanEatWhileHeydrichRegeneratesShouldBeAbleToConsumeMealAndSetRestrictionItem() {
        // GIVEN
        final String meal = "2000";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("63");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "4021")).andReturn(false);
        expect(itemHandler.addItem(character, "4021", 1)).andReturn(1);
        consumeItem(meal);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, meal);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenDrinkingWhileHeydrichRegeneratesShouldBeAbleToDrink() {
        // GIVEN
        final String meal = "2001";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("63");
        expect(wrapper.getCharacter()).andReturn(character);
        consumeItem(meal);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, meal);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenEatingAtRandomPlaceShouldBeAbleToEat() {
        // GIVEN
        final String meal = "2000";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        consumeItem(meal);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, meal);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenTryingToActivateSpellButOutOfTimeShouldNotAllowSpellActivation() {
        // GIVEN
        final String item = "4216";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(0);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenTryingToActivateSpellButAIsAlreadyActiveShouldNotAllowSpellActivation() {
        // GIVEN
        final String item = "4216";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(true);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenTryingToActivateSpellButBIsAlreadyActiveShouldNotAllowSpellActivation() {
        // GIVEN
        final String item = "4216";
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("100");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraph.getActions()).andReturn(3);
        paragraph.setActions(2);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4013")).andReturn(true);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenTryingToActivateSpellButCIsAlreadyActiveShouldNotAllowSpellActivation() {
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
        expect(itemHandler.hasItem(character, "4015")).andReturn(true);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenActivatingLuckSpellWhileNotFightingShouldAllowSpellActivation() {
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
        expect(character.getCommandView()).andReturn(null);
        character.changeLuck(3);
        itemHandler.removeItem(character, item, 1);
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, item);
        // THEN
        Assert.assertNull(returned);
    }

    private void consumeItem(final String consumeId) {
        expectWrapper();
        itemInteractionRecorder.recordItemConsumption(wrapper, consumeId);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(itemHandler.getItem(character, consumeId)).andReturn(item);
        expect(paragraph.getActions()).andReturn(9);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(8);
        itemHandler.consumeItem(character, consumeId, attributeHandler);
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
