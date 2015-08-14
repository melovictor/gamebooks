package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.service.FfMarketHandler;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;

import java.util.HashMap;
import java.util.Map;

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
 * Unit test for class {@link FfBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookTakeItemControllerPositiveHasTimeTest {

    private FfBookTakeItemController underTest;
    private IMocksControl mockControl;
    private HttpServletRequest request;
    private HttpSession session;
    private BeanFactory beanFactory;
    private HttpSessionWrapper wrapper;
    private FfCharacter character;
    private BookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private CommandView commandView;
    private FfAttributeHandler attributeHandler;
    private Paragraph paragraph;
    private FfParagraphData data;
    private FfMarketHandler marketHandler;
    private Map<String, Object> resultMap;
    private FfItem item;
    private ItemInteractionRecorder itemInteractionRecorder;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FfBookTakeItemController();
        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(FfCharacter.class);
        characterHandler = new FfCharacterHandler();
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setItemHandler(itemHandler);
        paragraph = mockControl.createMock(Paragraph.class);
        data = mockControl.createMock(FfParagraphData.class);
        commandView = mockControl.createMock(CommandView.class);
        marketHandler = mockControl.createMock(FfMarketHandler.class);
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);
        item = mockControl.createMock(FfItem.class);
        resultMap = new HashMap<>();
        info = new FfBookInformations(9L);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
        Whitebox.setInternalState(underTest, "marketHandler", marketHandler);
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        characterHandler.setCanEatEverywhere(true);
        mockControl.reset();
    }

    public void testHandleItemStateChangeWhenHasStateShouldRequestItemStateChange() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        paragraph.setActions(9);
        expect(wrapper.getCharacter()).andReturn(character);
        itemHandler.setItemEquipState(character, "3009", true);
        itemInteractionRecorder.changeItemEquipState(wrapper, "3009");
        mockControl.replay();
        // WHEN
        underTest.handleItemStateChange(request, "3009", true);
        // THEN
    }

    public void testHandleConsumeItemWhenFightingShouldDoNothing() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffFightSingle").times(2);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanEatEverywhereShouldConsumeItem() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(paragraph.getData()).andReturn(data);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        itemHandler.consumeItem(character, "2000", attributeHandler);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanEatHereShouldConsumeItem() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(true);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        itemHandler.consumeItem(character, "2000", attributeHandler);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanNotEatHereShouldDoNothing() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleConsumeItemWhenNoViewNameIsAvailableAndCanEatHereShouldConsumeItem() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(commandView.getViewName()).andReturn(null);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(true);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        itemHandler.consumeItem(character, "2000", attributeHandler);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleConsumeItemWhenNoEventIsOngoingAndCanEatHereShouldConsumeItem() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(true);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        itemHandler.consumeItem(character, "2000", attributeHandler);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleMarketBuyWhenInputDataIsCorrectShouldCallMarketHandler() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(marketHandler.handleMarketPurchase("3001", character, data, itemHandler)).andReturn(resultMap);
        itemInteractionRecorder.recordItemMarketMovement(wrapper, "Sale", "3001");
        mockControl.replay();
        // WHEN
        final Map<String, Object> response = underTest.handleMarketBuy(request, "3001");
        // THEN
        Assert.assertSame(response, resultMap);
    }

    public void testHandleMarketSellWhenInputDataIsCorrectShouldCallMarketHandler() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(marketHandler.handleMarketSell("3001", character, data, itemHandler)).andReturn(resultMap);
        itemInteractionRecorder.recordItemMarketMovement(wrapper, "Purchase", "3001");
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell(request, "3001");
        // THEN
        Assert.assertSame(returned, resultMap);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
