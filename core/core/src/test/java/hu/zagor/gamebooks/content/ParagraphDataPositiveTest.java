package hu.zagor.gamebooks.content;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ParagraphData}.
 * @author Tamas_Szekeres
 */
@Test
public class ParagraphDataPositiveTest {

    private static final String TEXT_WITH_GATERABLE_ITEMS = "There is [span class=\"takeItem\" data-"
        + "id=\"3002\" data-amount=\"1\"]one gatheredLostItem[/span] and later [span " + "class=\"takeItem\" data-amount=\"6\" data-id=\"3001\"]six others[/span].";

    private ParagraphData underTest;
    private Paragraph paragraph;
    private ChoicePositionComparator choiceComparator;
    private Logger logger;
    private IMocksControl mockControl;
    private Choice choiceA;
    private Choice choiceB;
    private GatheredLostItem gatheredLostItem;
    private Command command;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        paragraph = mockControl.createMock(Paragraph.class);
        choiceComparator = mockControl.createMock(ChoicePositionComparator.class);
        gatheredLostItem = mockControl.createMock(GatheredLostItem.class);
        choiceA = mockControl.createMock(Choice.class);
        choiceB = mockControl.createMock(Choice.class);
        command = mockControl.createMock(Command.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ParagraphData();
        underTest.setText("");
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setChoices(new DefaultChoiceSet(choiceComparator));
        mockControl.reset();
    }

    public void testCalculateValidElementsWhenUnderTestContainsNoChoicesItemsCommandsShouldNotExtractValidMoves() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        // THEN
        Assert.assertTrue(true);
    }

    public void testCalculateValidElementsWhenUnderTestContainsChoicesShouldExtractThemAsValidMoves() {
        // GIVEN
        expect(choiceComparator.compare(choiceA, choiceA)).andReturn(0);
        expect(choiceComparator.compare(choiceB, choiceA)).andReturn(1);
        expect(choiceA.getId()).andReturn("1");
        expect(choiceA.getText()).andReturn(null);
        expect(choiceA.getPosition()).andReturn(0);
        logger.debug("Found choice #{}[@{}]: '{}'", "1", "0", "");
        paragraph.addValidMove("1");
        expect(choiceB.getId()).andReturn("4");
        expect(choiceB.getText()).andReturn("Option B");
        expect(choiceB.getPosition()).andReturn(1);
        logger.debug("Found choice #{}[@{}]: '{}'", "4", "1", "Option B");
        paragraph.addValidMove("4");
        mockControl.replay();
        // WHEN
        underTest.addChoice(choiceA);
        underTest.addChoice(choiceB);
        underTest.calculateValidEvents(paragraph);
        // THEN
        Assert.assertTrue(true);
    }

    public void testAddChoicesWhenElementSetIsAddedShouldAllElementAppearInChoiceSet() {
        // GIVEN
        final Set<Choice> choices = new HashSet<>();
        choices.add(choiceA);
        choices.add(choiceB);
        expect(choiceComparator.compare(anyObject(Choice.class), anyObject(Choice.class))).andReturn(1).times(2);
        mockControl.replay();
        // WHEN
        underTest.addChoices(choices);
        final Set<Choice> returned = underTest.getChoices();
        // THEN
        Assert.assertEquals(returned.size(), 2);
        Choice containedA;
        Choice containedB;
        final Iterator<Choice> iterator = returned.iterator();
        containedA = iterator.next();
        containedB = iterator.next();
        Assert.assertTrue((containedA == choiceA && containedB == choiceB) || (containedA == choiceB && containedB == choiceA));
    }

    public void testCalculateValidElementsWhenUnderTestTextFieldContainsGatherableItemsShouldExtractThem() {
        // GIVEN
        setUpCalculation();
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        // THEN
        Assert.assertTrue(true);
    }

    public void testRemoveValidItemWhenSingleItemIsGatheredShouldRemoveLinkFromAroundText() {
        setUpCalculation();
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        underTest.removeValidItem("3002", 1);
        // THEN
        Assert.assertTrue(underTest.getText().contains("is one gatheredLostItem and"));
    }

    public void testRemoveValidItemWhenSingleItemIsGatheredAndMoreIsRemovedShouldRemoveLinkFromAroundText() {
        setUpCalculation();
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        underTest.removeValidItem("3002", 4);
        // THEN
        Assert.assertTrue(underTest.getText().contains("is one gatheredLostItem and"));
    }

    public void testRemoveValidItemWhenMultipleItemsAreGatheredButNotAllShouldReduceAmountFromAroundText() {
        setUpCalculation();
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        underTest.removeValidItem("3001", 4);
        // THEN
        Assert.assertTrue(underTest.getText().contains("class=\"takeItem\" data-amount=\"2\" data-id=\"3001\">six others</span>."));
    }

    public void testFixTextWhenTextIsNullShouldSetNullText() {
        // GIVEN
        underTest.setText(null);
        mockControl.replay();
        // WHEN
        final String returned = underTest.getText();
        // THEN
        Assert.assertNull(returned);
    }

    public void testAddGatheredItemWhenGatheredItemIsAddedShouldAppearInListOfGatheredItems() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addGatheredItem(gatheredLostItem);
        final List<GatheredLostItem> returned = underTest.getGatheredItems();
        // THEN
        Assert.assertTrue(returned.contains(gatheredLostItem));
    }

    public void testAddLostItemWhenLostItemIsAddedShouldAppearInListOfLostItems() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addLostItem(gatheredLostItem);
        final List<GatheredLostItem> returned = underTest.getLostItems();
        // THEN
        Assert.assertTrue(returned.contains(gatheredLostItem));
    }

    public void testAppendTextWhenTextIsNullShouldDoNothing() {
        // GIVEN
        underTest.setText(TEXT_WITH_GATERABLE_ITEMS);
        mockControl.replay();
        // WHEN
        underTest.appendText(null);
        final String returned = underTest.getText();
        // THEN
        Assert.assertEquals(returned.length(), TEXT_WITH_GATERABLE_ITEMS.length());
    }

    public void testAppendTextWhenTextIsNotNullShouldAppendText() {
        // GIVEN
        final String appendedText = "Some more text.";
        underTest.setText(TEXT_WITH_GATERABLE_ITEMS);
        mockControl.replay();
        // WHEN
        underTest.appendText(appendedText);
        final String returned = underTest.getText();
        // THEN
        Assert.assertEquals(returned.length(), TEXT_WITH_GATERABLE_ITEMS.length() + appendedText.length());
    }

    public void testCommandWhenCommandIsAddedShouldAppearInListOfCommands() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addCommand(command);
        final List<Command> returned = underTest.getCommands();
        // THEN
        Assert.assertTrue(returned.contains(command));
    }

    public void testGetPositionCounterShouldReturnSettedObject() {
        // GIVEN
        final ChoicePositionCounter expected = mockControl.createMock(ChoicePositionCounter.class);
        underTest.setPositionCounter(expected);
        mockControl.replay();
        // WHEN
        final ChoicePositionCounter returned = underTest.getPositionCounter();
        // THEN
        Assert.assertSame(returned, expected);
    }

    public void testCalculateValidElementsWhenUnderTestContainsNonUiCommandShouldNotExtractCommandMoves() {
        // GIVEN
        final Command command = new ItemCheckCommand();
        underTest.getCommands().add(command);
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        // THEN
        Assert.assertTrue(true);
    }

    public void testCalculateValidElementsWhenUnderTestContainsUiCommandShouldExtractItsValidCommand() {
        // GIVEN
        final Command command = new UserInputCommand();
        logger.debug("Found command '{}'.", "userInputResponse");
        paragraph.addValidMove("userInputResponse");
        underTest.addCommand(command);
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(paragraph);
        // THEN
        Assert.assertTrue(true);
    }

    public void testAddHiddenItemsShouldAddItemToHiddenList() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addHiddenItem(gatheredLostItem);
        // THEN
        Assert.assertTrue(underTest.getHiddenItems().contains(gatheredLostItem));
    }

    public void testAddUnhiddenItemsShouldAddItemToHiddenList() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addUnhiddenItem(gatheredLostItem);
        // THEN
        Assert.assertTrue(underTest.getUnhiddenItems().contains(gatheredLostItem));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private void setUpCalculation() {
        underTest.setText(TEXT_WITH_GATERABLE_ITEMS);
        logger.debug("Found item {}.", "3002");
        paragraph.addValidItem("3002", 1);
        logger.debug("Found item {}.", "3001");
        paragraph.addValidItem("3001", 6);
    }

}
