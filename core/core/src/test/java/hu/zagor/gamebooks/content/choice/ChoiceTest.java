package hu.zagor.gamebooks.content.choice;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Choice}.
 * @author Tamas_Szekeres
 */
@Test
public class ChoiceTest {

    private static final int POSITION = 11;
    private static final String TEXT = "text";
    private static final String FORMATTED_TEXT = "[span]text[/span]";
    private static final String FORMATTED_TRANSFORMED_TEXT = "<span>text</span>";
    private static final String ID = "110";
    private static final String DISPLAY = "110a";
    private static final String SINGLE_TEXT = "single text";
    private Choice underTest;
    private Choice choice;

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new Choice(null, TEXT, POSITION, null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenPositionIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new Choice(ID, TEXT, null, null).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenNoParametersSetShouldCreateEmptyObject() {
        // GIVEN
        // WHEN
        choice = new Choice();
        // THEN
        Assert.assertEquals(choice.getId(), "");
        Assert.assertEquals(choice.getText(), "");
        Assert.assertEquals(choice.getPosition(), 0);
    }

    public void testConstructorWhenTextIsNullShouldCreateBeanWithNullText() {
        // GIVEN
        underTest = new Choice(ID, null, POSITION, null);
        // WHEN
        final String returned = underTest.getText();
        // THEN
        Assert.assertNull(returned);
    }

    public void testConstructorWhenTextIsFormattedShouldCreateBeanWithTransformedText() {
        // GIVEN
        underTest = new Choice(ID, FORMATTED_TEXT, POSITION, null);
        // WHEN
        final String returned = underTest.getText();
        // THEN
        Assert.assertEquals(returned, FORMATTED_TRANSFORMED_TEXT);
    }

    public void testCloneShouldReturnClonedElement() throws CloneNotSupportedException {
        // GIVEN
        underTest = new Choice(ID, TEXT, POSITION, null);
        // WHEN
        final Choice returned = underTest.clone();
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getText(), TEXT);
        Assert.assertEquals(returned.getPosition(), POSITION);
        Assert.assertEquals(underTest.getDisplay(), ID);
    }

    public void testGetDisplayShouldReturnWhatWasSet() {
        // GIVEN
        underTest = new Choice(ID, TEXT, POSITION, null);
        // WHEN
        underTest.setDisplay(DISPLAY);
        // THEN
        Assert.assertEquals(underTest.getDisplay(), DISPLAY);
    }

    public void testGetSingleChoiceTextWhenSingleChoiceTextIsNullShouldNotSwitchToSingleMode() {
        // GIVEN
        underTest = new Choice(ID, TEXT, POSITION, null);
        // WHEN
        underTest.getSingleChoiceText();
        // THEN
        final String text = underTest.getText();
        Assert.assertEquals(text, TEXT);
    }

    public void testGetSingleChoiceTextWhenSingleChoiceTextIsNotNullShouldSwitchToSingleMode() {
        // GIVEN
        underTest = new Choice(ID, TEXT, POSITION, SINGLE_TEXT);
        // WHEN
        final String returned = underTest.getSingleChoiceText();
        // THEN
        final String text = underTest.getText();
        Assert.assertNull(text);
        Assert.assertSame(returned, SINGLE_TEXT);
    }
}
