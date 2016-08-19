package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.complex.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.service.FfMarketHandler;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.support.messages.MessageSource;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
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
    @Instance private BuySellResponse resultMap;
    @Mock private FfItem item;
    @Mock private MarketCommand marketCommand;
    @Mock private List<ProcessableItemHolder> holderList;
    @Mock private ProcessableItemHolder holder;
    @Inject private MessageSource messageSource;

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
    }

    public void testHandleItemStateChangeWhenHasStateShouldRequestItemStateChange() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        paragraph.setActions(9);
        expect(wrapper.getCharacter()).andReturn(character);
        itemHandler.setItemEquipState(character, "3009", true);
        mockControl.replay();
        // WHEN
        underTest.handleItemStateChange(request, "3009", true);
        // THEN
    }

    public void testHandleConsumeItemWhenFightingShouldDoNothing() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn("ffFightSingle").times(2);
        expect(messageSource.getMessage("page.ff.equipment.eat.notWhileFighting")).andReturn("Cannot eat while fighting");
        mockControl.replay();
        // WHEN
        final ConsumeItemResponse returned = underTest.handleConsumeItem(request, "2000");
        // THEN
        Assert.assertEquals(returned.getMessage(), "Cannot eat while fighting");
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanEatEverywhereShouldConsumeItem() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        characterHandler.setCanEatEverywhere(false);
        expect(data.isCanEat()).andReturn(false);
        expect(messageSource.getMessage("page.ff.equipment.eat.notAllowedEatingHere")).andReturn("Cannot eat here");
        mockControl.replay();
        // WHEN
        final ConsumeItemResponse returned = underTest.handleConsumeItem(request, "2000");
        // THEN
        Assert.assertEquals(returned.getMessage(), "Cannot eat here");
    }

    public void testHandleConsumeItemWhenNoViewNameIsAvailableAndCanEatHereShouldConsumeItem() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getItemsToProcess()).andReturn(holderList);
        expect(holderList.get(0)).andReturn(holder);
        expect(holder.getCommand()).andReturn(marketCommand);
        expect(marketHandler.handleMarketPurchase("3001", character, marketCommand, characterHandler)).andReturn(resultMap);
        mockControl.replay();
        // WHEN
        final BuySellResponse response = underTest.handleMarketBuy(request, "3001");
        // THEN
        Assert.assertSame(response, resultMap);
    }

    public void testHandleMarketSellWhenInputDataIsCorrectShouldCallMarketHandler() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getItemsToProcess()).andReturn(holderList);
        expect(holderList.get(0)).andReturn(holder);
        expect(holder.getCommand()).andReturn(marketCommand);
        expect(marketHandler.handleMarketSell("3001", character, marketCommand, characterHandler)).andReturn(resultMap);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell(request, "3001");
        // THEN
        Assert.assertSame(returned, resultMap);
    }

}
