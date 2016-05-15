package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckItemCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckItemCommandResolverTest {

    private static final String ID = "3001";
    @UnderTest private ItemCheckItemCommandResolver underTest;
    @MockControl private IMocksControl mockControl;
    @Instance private Character character;
    @Mock private ItemCheckCommand parent;
    @Mock private ParagraphData data;
    @Instance private CharacterHandler characterHandler;
    @Mock private CharacterItemHandler itemHandler;
    private BookInformations info;
    private Paragraph paragraph;
    @Inject private Logger logger;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setItemHandler(itemHandler);
        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(data);
    }

    public void testResolveWhenCharacterHasEquippedItemShouldReturnCommandsHaveEquippedBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(1);
        expect(itemHandler.hasEquippedItem(character, ID)).andReturn(true);
        logger.info("Player has single equipped item '{}'.", ID);
        expect(parent.getHaveEquipped()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterHasItemShouldReturnCommandsHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(1);
        expect(itemHandler.hasEquippedItem(character, ID)).andReturn(false);
        expect(itemHandler.hasItem(character, ID, 1)).andReturn(true);
        logger.info("Player has item '{}'.", ID);
        expect(parent.getHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterHasProperAmountOfItemsShouldSkipHaveEquippedAndReturnCommandsHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(3);
        expect(itemHandler.hasItem(character, ID, 3)).andReturn(true);
        logger.info("Player has item '{}'.", ID);
        expect(parent.getHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterDontHaveItemShouldReturnCommandsDontHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(1);
        expect(itemHandler.hasEquippedItem(character, ID)).andReturn(false);
        expect(itemHandler.hasItem(character, ID, 1)).andReturn(false);
        logger.info("Player doesn't have item '{}'.", ID);
        expect(parent.getDontHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    private ResolvationData getResolvationData() {
        return DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
    }
}
