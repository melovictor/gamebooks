package hu.zagor.gamebooks.content.choice;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultChoiceSet}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultChoiceSetTest {

    private DefaultChoiceSet underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new DefaultChoiceSet(new Comparator<Choice>() {

            @Override
            public int compare(final Choice o1, final Choice o2) {
                return o1.getPosition() > o2.getPosition() ? 1 : -1;
            }
        });
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest.clear();
    }

    public void testConstructorWhenNoParameterSetThenShouldCreateDefault() {
        // GIVEN
        // WHEN
        final DefaultChoiceSet defaultChoiceSet = new DefaultChoiceSet();
        // THEN
        final Map<Choice, Object> map = Whitebox.<TreeMap<Choice, Object>>getInternalState(defaultChoiceSet, "m");
        final Comparator<Choice> comparator = Whitebox.<Comparator<Choice>>getInternalState(map, "comparator");
        Assert.assertTrue(comparator instanceof ChoicePositionComparator);
    }

    public void testGetChoiceByPositionWhenPositionIsNotPresentShouldReturnNull() {
        // GIVEN
        underTest.add(new Choice("id", "text", 11, null));
        underTest.add(new Choice("id2", "text2", 22, null));
        // WHEN
        final Choice returned = underTest.getChoiceByPosition(0);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetChoiceByPositionWhenPositionIsPresentShouldReturnChoice() {
        // GIVEN
        final Choice choice = new Choice("id", "text", 11, null);
        underTest.add(choice);
        underTest.add(new Choice("id2", "text2", 22, null));
        // WHEN
        final Choice returned = underTest.getChoiceByPosition(11);
        // THEN
        Assert.assertSame(returned, choice);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetChoiceByIdWhenParagraphIdIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.getChoiceById(null);
        // THEN throws exception
    }

    public void testGetChoiceByIdWhenParagraphIdIsNotPresentInSetShouldReturnNull() {
        // GIVEN
        underTest.add(new Choice("id", "text", 11, null));
        underTest.add(new Choice("id2", "text2", 22, null));
        underTest.add(new Choice("id3", "text2", 33, null));
        // WHEN
        final Choice returned = underTest.getChoiceById("99");
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetChoiceByIdWhenParagraphIdIsPresentInSetShouldReturnProperChoice() {
        // GIVEN
        underTest.add(new Choice("id", "text", 11, null));
        final Choice choice = new Choice("id2", "text2", 22, null);
        underTest.add(choice);
        underTest.add(new Choice("id3", "text2", 33, null));
        // WHEN
        final Choice returned = underTest.getChoiceById("id2");
        // THEN
        Assert.assertSame(returned, choice);
    }

    public void testRemoveByPositionWhenChoiceWithPositionIsPresentShouldReturnChoiceAndRemoveItFromSet() {
        // GIVEN
        underTest.add(new Choice("id", "text", 11, null));
        final Choice choice = new Choice("id2", "text2", 22, null);
        underTest.add(choice);
        underTest.add(new Choice("id3", "text2", 33, null));
        // WHEN
        final Choice returned = underTest.removeByPosition(22);
        // THEN
        Assert.assertSame(returned, choice);
        Assert.assertFalse(underTest.contains(choice));
    }

    public void testRemoveByPositionWhenChoiceWithPositionIsNotPresentShouldReturnNull() {
        // GIVEN
        underTest.add(new Choice("id", "text", 11, null));
        final Choice choice = new Choice("id2", "text2", 22, null);
        underTest.add(choice);
        underTest.add(new Choice("id3", "text2", 33, null));
        // WHEN
        final Choice returned = underTest.removeByPosition(44);
        // THEN
        Assert.assertNull(returned);
    }

}
