package hu.zagor.gamebooks.ff.ff.mom.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.complex.mvc.book.inventory.domain.ConsumeItemResponse;
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
import java.util.List;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff23BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff23BookTakeItemControllerConsumeItemTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff23BookTakeItemController underTest;
    @Mock private HttpSession session;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;
    @Mock private Paragraph paragraph;
    @Mock private FfCharacter character;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfParagraphData data;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfItem item;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private List<Item> itemList;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setCanEatEverywhere(true);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
    }

    public void testDoHandleConsumeItemWhenNormalItemShouldConsumeNormally() {
        // GIVEN
        final String itemId = "2002";
        itemInteractionRecorder.recordItemConsumption(wrapper, itemId);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(itemHandler.getItem(character, itemId)).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.potion);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn(itemId);
        itemHandler.consumeItem(character, itemId, attributeHandler);
        mockControl.replay();
        // WHEN
        final ConsumeItemResponse returned = underTest.doHandleConsumeItem(wrapper, itemId);
        // THEN
        Assert.assertNull(returned.getMessage());
        Assert.assertNull(returned.getOnclick());
    }

    public void testDoHandleConsumeItemWhenProvisionShouldRemoveNotEatenFlagAndConsumeNormally() {
        // GIVEN
        final String itemId = "2000";
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.removeItem(character, "4002", 10)).andReturn(itemList);
        itemInteractionRecorder.recordItemConsumption(wrapper, itemId);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(itemHandler.getItem(character, itemId)).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.provision);
        expect(paragraph.getData()).andReturn(data);
        expect(paragraph.getActions()).andReturn(10);
        expect(item.getActions()).andReturn(1);
        paragraph.setActions(9);
        expect(item.getId()).andReturn(itemId);
        itemHandler.consumeItem(character, itemId, attributeHandler);
        mockControl.replay();
        // WHEN
        final ConsumeItemResponse returned = underTest.doHandleConsumeItem(wrapper, itemId);
        // THEN
        Assert.assertNull(returned.getMessage());
        Assert.assertNull(returned.getOnclick());
    }

}
