package hu.zagor.gamebooks.recording;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultItemInteractionRecorder}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultItemInteractionRecorderTest {

    private static final String ITEM_ID = "1010";
    private IMocksControl mockControl;
    private DefaultItemInteractionRecorder underTest;
    private EnvironmentDetector environmentDetector;
    private HttpSessionWrapper wrapper;
    private Character character;
    private Note notes;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(Character.class);
        notes = mockControl.createMock(Note.class);
        underTest = new DefaultItemInteractionRecorder();
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testRecordItemTakingWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordItemTaking(wrapper, ITEM_ID);
        // THEN
    }

    public void testRecordItemReplacingWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordItemReplacing(wrapper, ITEM_ID, "1020");
        // THEN
    }

    public void testRecordItemConsumptionWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordItemConsumption(wrapper, ITEM_ID);
        // THEN
    }

    public void testChangeItemEquipStateWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.changeItemEquipState(wrapper, ITEM_ID);
        // THEN
    }

    public void testRecordItemMarketMovementWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordItemMarketMovement(wrapper, "sell", ITEM_ID);
        // THEN
    }

    public void testRecordItemTakingWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ntakeItem(\"1010\");\n");
        mockControl.replay();
        // WHEN
        underTest.recordItemTaking(wrapper, ITEM_ID);
        // THEN
    }

    public void testRecordItemConsumptionWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nconsumeItem(\"1010\");\n");
        mockControl.replay();
        // WHEN
        underTest.recordItemConsumption(wrapper, ITEM_ID);
        // THEN
    }

    public void testChangeItemEquipStateWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\napplyItem(\"1010\");\n");
        mockControl.replay();
        // WHEN
        underTest.changeItemEquipState(wrapper, ITEM_ID);
        // THEN
    }

    public void testRecordItemMarketMovementWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nmarketing(\"sell\", \"1010\");\n");
        mockControl.replay();
        // WHEN
        underTest.recordItemMarketMovement(wrapper, "sell", ITEM_ID);
        // THEN
    }

    public void testRecordItemReplacingWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nreplaceItem(\"1010\", \"1020\");\n");
        mockControl.replay();
        // WHEN
        underTest.recordItemReplacing(wrapper, ITEM_ID, "1020");
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
