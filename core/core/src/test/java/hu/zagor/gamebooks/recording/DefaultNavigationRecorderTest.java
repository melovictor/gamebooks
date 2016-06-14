package hu.zagor.gamebooks.recording;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.Note;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.ui.Model;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultNavigationRecorder}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultNavigationRecorderTest {

    private DefaultNavigationRecorder underTest;
    private IMocksControl mockControl;
    private EnvironmentDetector environmentDetector;
    private HttpSessionWrapper wrapper;
    private Character character;
    private Note notes;
    private Paragraph previousParagraph;
    private Paragraph paragraph;
    private ParagraphData data;
    private ChoiceSet choices;
    private Choice choice;
    private ChoiceSet choiceWithActualElements;
    private ChoiceSet choiceWithNoElements;
    private Model model;
    private CommandList commandList;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(Character.class);
        notes = mockControl.createMock(Note.class);
        previousParagraph = mockControl.createMock(Paragraph.class);
        paragraph = mockControl.createMock(Paragraph.class);
        choiceWithNoElements = new DefaultChoiceSet(new ChoicePositionComparator());
        choiceWithActualElements = new DefaultChoiceSet(new ChoicePositionComparator());
        choiceWithActualElements.add(new Choice("11", null, 0, null));
        choiceWithActualElements.add(new Choice("43", null, 1, null));
        data = mockControl.createMock(ParagraphData.class);
        choices = mockControl.createMock(ChoiceSet.class);
        choice = mockControl.createMock(Choice.class);
        model = mockControl.createMock(Model.class);
        commandList = mockControl.createMock(CommandList.class);
        underTest = new DefaultNavigationRecorder();
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testRecordNavigationWhenNotInRecordStateShouldDoNothing() {
        // GIVEN
        expect(wrapper.getModel()).andReturn(model);
        expect(model.addAttribute("environmentDetector", environmentDetector)).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.recordNavigation(wrapper, "22", previousParagraph, paragraph);
        // THEN
    }

    public void testRecordNavigationWhenInRecordStateButSectionIdentifierIsNullAndHasNoChoicesVisibleShouldDoNothing() {
        // GIVEN
        expect(wrapper.getModel()).andReturn(model);
        expect(model.addAttribute("environmentDetector", environmentDetector)).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(true);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choiceWithNoElements);
        mockControl.replay();
        // WHEN
        underTest.recordNavigation(wrapper, null, previousParagraph, paragraph);
        // THEN
    }

    public void testRecordNavigationWhenInRecordStateButSectionIdentifierIsNullAndHasChoicesVisibleShouldRecordOnlyVerifyPosition() {
        // GIVEN
        expect(wrapper.getModel()).andReturn(model);
        expect(model.addAttribute("environmentDetector", environmentDetector)).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(true);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choiceWithActualElements);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nverifyPosition(0, 11);\nverifyPosition(1, 43);\n");
        mockControl.replay();
        // WHEN
        underTest.recordNavigation(wrapper, null, previousParagraph, paragraph);
        // THEN
    }

    public void testRecordNavigationWhenAdminCharacterJumpsToInvalidLocationShouldAvoidNpeAndTryToRecordAsWellAsPossible() {
        // GIVEN
        expect(wrapper.getModel()).andReturn(model);
        expect(model.addAttribute("environmentDetector", environmentDetector)).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(previousParagraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(paragraph.getId()).andReturn("335");
        expect(choices.getChoiceById("335")).andReturn(null);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(true);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choiceWithActualElements);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\nverifyPosition(0, 11);\nverifyPosition(1, 43);\n");
        mockControl.replay();
        // WHEN
        underTest.recordNavigation(wrapper, "335", previousParagraph, paragraph);
        // THEN
    }

    public void testRecordNavigationWhenInRecordStateAndCommandListIsEmptyShouldRecordNavigationAndVerification() {
        // GIVEN
        expect(wrapper.getModel()).andReturn(model);
        expect(model.addAttribute("environmentDetector", environmentDetector)).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(previousParagraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(paragraph.getId()).andReturn("22");
        expect(choices.getChoiceById("22")).andReturn(choice);
        expect(choice.getPosition()).andReturn(1);
        expect(paragraph.getDisplayId()).andReturn("22");
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(true);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choiceWithActualElements);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ngoToPosition(1, 22);\nverifyPosition(0, 11);\nverifyPosition(1, 43);\n");
        mockControl.replay();
        // WHEN
        underTest.recordNavigation(wrapper, "s-22", previousParagraph, paragraph);
        // THEN
    }

    public void testRecordNavigationWhenInRecordStateAndCommandListIsNotEmptyShouldRecordNavigationOnly() {
        // GIVEN
        expect(wrapper.getModel()).andReturn(model);
        expect(model.addAttribute("environmentDetector", environmentDetector)).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(true);
        expect(previousParagraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(paragraph.getId()).andReturn("22");
        expect(choices.getChoiceById("22")).andReturn(choice);
        expect(choice.getPosition()).andReturn(1);
        expect(paragraph.getDisplayId()).andReturn("22");
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(false);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getNotes()).andReturn(notes);
        expect(notes.getNote()).andReturn("setUpGame();\n");
        character.setNotes("setUpGame();\ngoToPosition(1, 22);\n");
        mockControl.replay();
        // WHEN
        underTest.recordNavigation(wrapper, "s-22", previousParagraph, paragraph);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
