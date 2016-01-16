package hu.zagor.gamebooks.content.command.changeitem;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ChangeItemCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ChangeItemCommandResolverTest {

    private static final String ITEM_ID = "1005";
    @UnderTest private ChangeItemCommandResolver underTest;
    @MockControl private IMocksControl mockControl;
    @Instance private ChangeItemCommand command;
    @Instance private ResolvationData resolvationData;
    @Mock private FfCharacter character;
    private BookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    private FfItem item;
    private FfItem itemB;

    @BeforeClass
    public void setUpClass() {
        command = new ChangeItemCommand();
        resolvationData.setCharacter(character);
        info = new BookInformations(3L);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);
        command.setId(ITEM_ID);
        command.setAttribute("attackStrength");
    }

    @BeforeMethod
    public void setUpMethod() {
        command.setChangeValue(null);
        command.setNewValue(null);
        item = new FfItem(ITEM_ID, "Sword", ItemType.weapon1);
        itemB = new FfItem(ITEM_ID, "Sword", ItemType.weapon1);
        mockControl.reset();
    }

    public void testDoResolveWhenCharacterDoesNotHaveItemInQuestionShouldDoNothing() {
        // GIVEN
        expect(itemHandler.getItems(character, ITEM_ID)).andReturn(new ArrayList<Item>());
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoResolveWhenNewValueIsSetShouldSetNewValueToField() {
        // GIVEN
        command.setNewValue(3);
        expect(itemHandler.getItems(character, ITEM_ID)).andReturn(Arrays.asList((Item) item));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(item.getAttackStrength(), 3);
    }

    public void testDoResolveWhenChangedAmountIsSetShouldChangeFieldByAmount() {
        // GIVEN
        command.setChangeValue("-1");
        item.setAttackStrength(3);
        expect(itemHandler.getItems(character, ITEM_ID)).andReturn(Arrays.asList((Item) item));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(item.getAttackStrength(), 2);
    }

    public void testDoResolveWhenChangedAmountIsSetForMultipleItemsShouldChangeFieldByAmountForAllItems() {
        // GIVEN
        command.setChangeValue("-1");
        item.setAttackStrength(3);
        itemB.setAttackStrength(3);
        expect(itemHandler.getItems(character, ITEM_ID)).andReturn(Arrays.asList((Item) item, (Item) itemB));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(item.getAttackStrength(), 2);
        Assert.assertEquals(itemB.getAttackStrength(), 2);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
