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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookTakeItemControllerPositiveNotProvisionTest {
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

    public void testHandleConsumeItemWhenNoEventIsOngoingAndWeAreDrinkingShouldConsumeItem() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(itemHandler.getItem(character, "2001")).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.potion);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn("2001");
        itemHandler.consumeItem(character, "2001", attributeHandler);
        mockControl.replay();
        // WHEN
        underTest.handleConsumeItem(request, "2001");
        // THEN
    }

}
