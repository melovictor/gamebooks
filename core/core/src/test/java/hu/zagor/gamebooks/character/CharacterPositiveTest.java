package hu.zagor.gamebooks.character;

import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.CommandView;
import java.util.ArrayList;
import java.util.List;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Character}.
 * @author Tamas_Szekeres
 */
@Test
public class CharacterPositiveTest {

    private Character underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new Character();
    }

    public void testGetEquipmentShouldReturnEquipmentList() {
        // GIVEN
        // WHEN
        final List<Item> equipment = underTest.getEquipment();
        // THEN
        Assert.assertTrue(equipment instanceof ArrayList<?>);
    }

    public void testGetNotesShouldReturnSetString() {
        // GIVEN
        final String expected = "note";
        underTest.setNotes(expected);
        // WHEN
        final String returned = underTest.getNotes().getNote();
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testGetNotesAsStringShouldReturnSetString() {
        // GIVEN
        final String expected = "note";
        underTest.setNotes(expected);
        // WHEN
        final String returned = underTest.getNotes().toString();
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testGetCommandViewShouldReturnSettedObject() {
        // GIVEN
        final CommandView commandView = Whitebox.newInstance(CommandView.class);
        underTest.setCommandView(commandView);
        // WHEN
        final CommandView returned = underTest.getCommandView();
        // THEN
        Assert.assertSame(returned, commandView);
    }
}
