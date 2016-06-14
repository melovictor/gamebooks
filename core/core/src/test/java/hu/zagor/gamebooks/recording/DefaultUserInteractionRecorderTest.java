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
 * Unit test for class {@link DefaultUserInteractionRecorder}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultUserInteractionRecorderTest {

    private static final String ENEMY_ID = "3";
    private DefaultUserInteractionRecorder underTest;
    private EnvironmentDetector environmentDetector;
    private HttpSessionWrapper wrapper;
    private Character character;
    private Note notes;
    private IMocksControl mockControl;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(Character.class);
        notes = mockControl.createMock(Note.class);
        underTest = new DefaultUserInteractionRecorder();
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testRecordAttributeTestWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordAttributeTest(wrapper);
        // THEN
    }

    public void testRecordRandomRollWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordRandomRoll(wrapper);
        // THEN
    }

    public void testRecordFightCommandWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordFightCommand(wrapper, ENEMY_ID);
        // THEN
    }

    public void testPrepareFightCommandWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.prepareFightCommand(wrapper, true, false, false);
        // THEN
    }

    public void testRecordAttributeTestWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ndoAttributeCheck();\n");
        mockControl.replay();
        // WHEN
        underTest.recordAttributeTest(wrapper);
        // THEN
    }

    public void testRecordRandomRollWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ndoRandomRoll();\n");
        mockControl.replay();
        // WHEN
        underTest.recordRandomRoll(wrapper);
        // THEN
    }

    public void testRecordFightCommandWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nattackEnemy(\"" + ENEMY_ID + "\");\n");
        mockControl.replay();
        // WHEN
        underTest.recordFightCommand(wrapper, ENEMY_ID);
        // THEN
    }

    public void testPrepareFightCommandWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nprepareLuckSettings(true, false, false);\n");
        mockControl.replay();
        // WHEN
        underTest.prepareFightCommand(wrapper, true, false, false);
        // THEN
    }

    public void testRecordUserResponseWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordUserResponse(wrapper, "response", 3123);
        // THEN
    }

    public void testRecordUserResponseWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ngiveResponse(\"response\", 3123);\n");
        mockControl.replay();
        // WHEN
        underTest.recordUserResponse(wrapper, "response", 3123);
        // THEN
    }

    public void testRecordMarketClosingWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordMarketClosing(wrapper);
        // THEN
    }

    public void testRecordMarketClosingWhenInRecordStateShouldRecordEvent() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ncloseMarket();\n");
        mockControl.replay();
        // WHEN
        underTest.recordMarketClosing(wrapper);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
