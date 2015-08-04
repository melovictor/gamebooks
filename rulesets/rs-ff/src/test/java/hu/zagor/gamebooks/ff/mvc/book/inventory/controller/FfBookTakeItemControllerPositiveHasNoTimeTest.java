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
import hu.zagor.gamebooks.support.writer.Converter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookTakeItemControllerPositiveHasNoTimeTest {

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
    private Converter converter;
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
        item = mockControl.createMock(FfItem.class);
        converter = mockControl.createMock(Converter.class);
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);
        info = new FfBookInformations(9L);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
        Whitebox.setInternalState(underTest, "marketHandler", marketHandler);
        Whitebox.setInternalState(underTest, "converter", converter);
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        characterHandler.setCanEatEverywhere(true);
        mockControl.reset();
    }

    public void testHandleItemStateChangeWhenHasNoTimeShouldDoNothing() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(0);
        mockControl.replay();
        // WHEN
        underTest.handleItemStateChange(request, "3009", true);
        // THEN
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanEatButHasNoTimeShouldNotConsumeItem() {
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
        expect(paragraph.getActions()).andReturn(0);
        expect(item.getActions()).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2000");
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
