package hu.zagor.gamebooks.character;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Character}.
 * @author Tamas_Szekeres
 */
@Test
public class CharacterTest {

    private static final String SAMPLE_NOTE = "this is a note";
    @UnderTest private Character underTest;
    @MockControl private IMocksControl mockControl;
    @Instance(inject = true) private List<Item> hiddenEquipment;
    @Instance(inject = true) private List<Item> equipment;
    @Mock private Item itemA;
    @Mock private Item itemB;
    @Mock private Item itemC;
    @Mock private Item itemD;
    @Mock private Item itemAClone;
    @Mock private Item itemBClone;
    @Mock private Item itemCClone;
    @Mock private Item itemDClone;
    @Mock private Note notes;
    @Mock private Note notesClone;
    @Instance(inject = true) private List<String> paragraphs;
    @Instance(inject = true) private Set<String> codeWords;
    @Instance(inject = true) private Map<String, String> userInteraction;

    @BeforeClass
    public void setUpClass() {
        hiddenEquipment.add(itemA);
        hiddenEquipment.add(itemB);
        equipment.add(itemC);
        equipment.add(itemD);
        paragraphs.add("1");
        paragraphs.add("63");
        codeWords.add("ship");
        codeWords.add("crossbones");
        userInteraction.put("enemyId", "13");
    }

    public void testGetHiddenEquipmentShouldReturnHiddenEquipment() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final List<Item> returned = underTest.getHiddenEquipment();
        // THEN
        Assert.assertSame(returned, hiddenEquipment);
    }

    public void testSetNotesWhenOriginallyTheNotesIsNullShouldCreateNewOneAndThenSetTheValue() {
        // GIVEN
        Whitebox.setInternalState(underTest, "notes", (Note) null);
        mockControl.replay();
        // WHEN
        underTest.setNotes(SAMPLE_NOTE);
        // THEN
        Assert.assertSame(underTest.getNotes().getNote(), SAMPLE_NOTE);
    }

    public void testCloneShouldCreateCopy() throws CloneNotSupportedException {
        // GIVEN
        expect(itemC.clone()).andReturn(itemCClone);
        expect(itemD.clone()).andReturn(itemDClone);
        expect(itemA.clone()).andReturn(itemAClone);
        expect(itemB.clone()).andReturn(itemBClone);
        expect(notes.clone()).andReturn(notesClone);
        Whitebox.setInternalState(underTest, "notes", notes);
        mockControl.replay();
        // WHEN
        final Character returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
        Assert.assertEquals(returned.getCodeWords(), codeWords);
        Assert.assertNotSame(returned.getCodeWords(), codeWords);
        Assert.assertEquals(returned.getParagraphs(), paragraphs);
        Assert.assertNotSame(returned.getParagraphs(), paragraphs);
        Assert.assertEquals(returned.getUserInteraction(), userInteraction);
        Assert.assertNotSame(returned.getUserInteraction(), userInteraction);
        Assert.assertSame(returned.getNotes(), notesClone);
        Assert.assertEquals(returned.getEquipment(), Arrays.asList(itemCClone, itemDClone));
        Assert.assertEquals(returned.getHiddenEquipment(), Arrays.asList(itemAClone, itemBClone));
    }

}
