package hu.zagor.gamebooks.ff.ff.mom.mvc.books.inventory.controller;

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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff23BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff23BookTakeItemControllerConsumeItemTest {

    @MockControl
    private IMocksControl mockControl;
    @UnderTest
    private Ff23BookTakeItemController underTest;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Inject
    private BeanFactory beanFactory;
    @Mock
    private HttpSessionWrapper wrapper;
    @Inject
    private ItemInteractionRecorder itemInteractionRecorder;
    @Mock
    private Paragraph paragraph;
    @Mock
    private FfCharacter character;
    private FfBookInformations info;
    @Instance
    private FfCharacterHandler characterHandler;
    @Mock
    private FfParagraphData data;
    @Mock
    private FfCharacterItemHandler itemHandler;
    @Mock
    private FfItem item;
    @Mock
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setCanEatEverywhere(true);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoHandleConsumeItemWhenNormalItemShouldConsumeNormally() {
        // GIVEN
        expectConsumeItemNormally("2002");
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, "2002");
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoHandleConsumeItemWhenProvisionShouldRemoveNotEatenFlagAndConsumeNormally() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        itemHandler.removeItem(character, "4002", 10);
        expectConsumeItemNormally("2000");
        mockControl.replay();
        // WHEN
        final String returned = underTest.doHandleConsumeItem(request, "2000");
        // THEN
        Assert.assertNull(returned);
    }

    private void expectConsumeItemNormally(final String itemId) {
        expectWrapper();
        itemInteractionRecorder.recordItemConsumption(wrapper, itemId);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(paragraph.getData()).andReturn(data);
        expect(itemHandler.getItem(character, itemId)).andReturn(item);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        itemHandler.consumeItem(character, itemId, attributeHandler);
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
