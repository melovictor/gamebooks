package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ProcessableItemHolder}.
 * @author Tamas_Szekeres
 */
@Test
public class ProcessableItemHolderTest {

    private final ParagraphData paragraphData = new ParagraphData();
    private final Command command = new ItemCheckCommand();

    public void testIsParagraphDataHolderWhenParagraphDataIsSetShouldReturnTrue() {
        // GIVEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder(paragraphData);
        // WHEN
        final boolean returned = underTest.isParagraphDataHolder();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsParagraphDataHolderWhenCommandIsSetShouldReturnFalse() {
        // GIVEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder(command);
        // WHEN
        final boolean returned = underTest.isParagraphDataHolder();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testGetCommandWhenParagraphDataIsSetShouldReturnNull() {
        // GIVEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder(paragraphData);
        // WHEN
        final Command returned = underTest.getCommand();
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetParagraphDataWhenCommandIsSetShouldReturnNull() {
        // GIVEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder(command);
        // WHEN
        final ParagraphData returned = underTest.getParagraphData();
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetCommandWhenCommandIsSetShouldReturnCommand() {
        // GIVEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder(command);
        // WHEN
        final Command returned = underTest.getCommand();
        // THEN
        Assert.assertSame(returned, command);
    }

    public void testGetParagraphDataWhenParagraphDataIsSetShouldReturnParagraphData() {
        // GIVEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder(paragraphData);
        // WHEN
        final ParagraphData returned = underTest.getParagraphData();
        // THEN
        Assert.assertSame(returned, paragraphData);
    }

    public void testDefaultConstructorShouldCreateEmptyObject() {
        // GIVEN
        // WHEN
        final ProcessableItemHolder underTest = new ProcessableItemHolder();
        // THEN
        Assert.assertNull(underTest.getCommand());
        Assert.assertNull(underTest.getParagraphData());
    }

}
