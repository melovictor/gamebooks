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
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.messages.MessageSource;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
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
public class FfBookTakeItemControllerPositiveHasNoTimeTest {
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
    @Mock private FfItem item;
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

    public void testHandleItemStateChangeWhenHasNoTimeShouldDoNothing() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(0);
        mockControl.replay();
        // WHEN
        underTest.handleItemStateChange(request, "3009", true);
        // THEN
    }

    public void testHandleConsumeItemWhenLuckTestingAndCanEatButHasNoTimeShouldNotConsumeItem() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(itemHandler.getItem(character, "2000")).andReturn(item);
        expect(commandView.getViewName()).andReturn("ffAttributeTest").times(2);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        expect(paragraph.getActions()).andReturn(0);
        expect(item.getActions()).andReturn(1);
        expect(messageSource.getMessage("page.ff.equipment.eat.notEnoughActionPoints")).andReturn("No time to eat");
        mockControl.replay();
        // WHEN
        final ConsumeItemResponse returned = underTest.handleConsumeItem(request, "2000");
        // THEN
        Assert.assertEquals(returned.getMessage(), "No time to eat");
    }

}
