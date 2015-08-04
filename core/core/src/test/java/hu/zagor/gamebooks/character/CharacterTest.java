package hu.zagor.gamebooks.character;

import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.character.item.Item;

import java.util.ArrayList;
import java.util.List;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Character}.
 * @author Tamas_Szekeres
 */
@Test
public class CharacterTest {

    private static final String SAMPLE_NOTE = "this is a note";
    private Character underTest;

    public void testGetHiddenEquipmentShouldReturnHiddenEquipment() {
        // GIVEN
        underTest = new Character();
        final List<Item> hiddenEquipment = new ArrayList<Item>();
        Whitebox.setInternalState(underTest, "hiddenEquipment", hiddenEquipment);
        // WHEN
        final List<Item> returned = underTest.getHiddenEquipment();
        // THEN
        Assert.assertSame(returned, hiddenEquipment);
    }

    public void testSetNotesWhenOriginallyTheNotesIsNullShouldCreateNewOneAndThenSetTheValue() {
        // GIVEN
        underTest = new Character();
        Whitebox.setInternalState(underTest, "notes", (Note) null);
        // WHEN
        underTest.setNotes(SAMPLE_NOTE);
        // THEN
        Assert.assertSame(underTest.getNotes().getNote(), SAMPLE_NOTE);
    }

}
