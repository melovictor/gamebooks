package hu.zagor.gamebooks.books.contentransforming.section.commandstub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.stub.itemcheck.ItemCheckDontHaveTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link ItemCheckDontHaveTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckDontHaveTransformerTest {

    private ItemCheckDontHaveTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private Node node;
    private ItemCheckCommand command;
    private ChoicePositionCounter positionCounter;
    private ParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        command = mockControl.createMock(ItemCheckCommand.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        paragraphData = mockControl.createMock(ParagraphData.class);
        underTest = new ItemCheckDontHaveTransformer();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenParentIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(null, node, command, positionCounter);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, null, command, positionCounter);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenCommandIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, null, positionCounter);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenPositionCounterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, null);
        // THEN throws exception
    }

    public void testTransformWhenParametersAreCorrectShouldSetHaveEquipped() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        command.setDontHave(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, positionCounter);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
