package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
import hu.zagor.gamebooks.content.ParagraphData;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckSectionCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckSectionCommandTest {

    private static final String ID = "3001";
    private ItemCheckSectionCommand underTest;
    private IMocksControl mockControl;
    private Character character;
    private ItemCheckCommand parent;
    private ParagraphData data;
    private CharacterHandler characterHandler;
    private CharacterParagraphHandler paragraphHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(ItemCheckCommand.class);
        data = mockControl.createMock(ParagraphData.class);
        characterHandler = new CharacterHandler();
        paragraphHandler = mockControl.createMock(CharacterParagraphHandler.class);
        characterHandler.setParagraphHandler(paragraphHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ItemCheckSectionCommand();
        character = new Character();
        mockControl.reset();
    }

    public void testResolveWhenCharacterVisitedSectionShouldReturnCommandsHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(paragraphHandler.visitedParagraph(character, ID)).andReturn(true);
        expect(parent.getHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, character, characterHandler);
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterHasNotVisitedSectionShouldReturnCommandsDontHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(paragraphHandler.visitedParagraph(character, ID)).andReturn(false);
        expect(parent.getDontHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, character, characterHandler);
        // THEN
        Assert.assertSame(returned, data);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
