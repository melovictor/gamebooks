package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.service.FfMarketHandler;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
 * Unit test for class {@link FfBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookTakeItemControllerPositiveHasTimeTest {
    @UnderTest private FfBookTakeItemController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private HttpServletRequest request;
    @Mock private HttpSession session;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private FfCharacter character;
    private BookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private CommandView commandView;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private Paragraph paragraph;
    @Mock private FfParagraphData data;
    @Inject private FfMarketHandler marketHandler;
    @Instance private Map<String, Object> resultMap;
    @Mock private FfItem item;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setItemHandler(itemHandler);
        info = new FfBookInformations(9L);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
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
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
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
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn("2000");
        itemHandler.consumeItem(character, "2000", attributeHandler);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanEatHereShouldConsumeItem() {
        // GIVEN
        characterHandler.setCanEatEverywhere(false);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        itemInteractionRecorder.recordItemConsumption(wrapper, "2000");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        expect(data.isCanEat()).andReturn(true);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn("2000");
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
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(item.getItemType()).andReturn(ItemType.provision);
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
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn(null);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(true);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn("2000");
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
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(true);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn("2000");
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
