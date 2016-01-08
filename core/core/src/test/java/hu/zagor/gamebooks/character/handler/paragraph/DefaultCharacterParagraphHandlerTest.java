package hu.zagor.gamebooks.character.handler.paragraph;

import hu.zagor.gamebooks.character.Character;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultCharacterParagraphHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultCharacterParagraphHandlerTest {

    private static final String PARAGRAPH_ID = "11";
    private CharacterParagraphHandler underTest;
    private Character character;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultCharacterParagraphHandler();
        character = new Character();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddParagraphWhenParagraphIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.addParagraph(character, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVisitedParagraphWhenParagraphIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.visitedParagraph(character, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddParagraphWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.addParagraph(null, PARAGRAPH_ID);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testVisitedParagraphWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.visitedParagraph(null, PARAGRAPH_ID);
        // THEN throws exception
    }

    public void testVisitedParagraphWhenParagraphIsAddedShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, PARAGRAPH_ID);
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, PARAGRAPH_ID);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsNotAddedShouldReturnFalse() {
        // GIVEN
        underTest.addParagraph(character, "10");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, PARAGRAPH_ID);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleOrAndBothAreVisitedShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "394");
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 or 314");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleOrAndFirstIsVisitedShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "394");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 or 314");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleOrAndSecondIsVisitedShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 or 314");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleOrAndNeitherIsVisitedShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394    or   314");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleAndAndBothAreVisitedShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "394");
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "  394   and   314  ");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleAndAndFirstIsVisitedShouldReturnFalse() {
        // GIVEN
        underTest.addParagraph(character, "394");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 and 314");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleAndAndSecondIsVisitedShouldReturnFalse() {
        // GIVEN
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 and 314");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenParagraphIsSimpleAndAndNeitherIsVisitedShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 and 314");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenParagraphIsComplexAndVisitedOnShortSideShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "394");
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 or (180 and 314)");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsComplexAndVisitedOnLongSideShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "180");
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 or (180 and 314)");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsComplexAndVisitedEnoughShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "180");
        underTest.addParagraph(character, "314");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394 or 180 and 314");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenParagraphIsComplexAndNotVisitedShouldReturnFalse() {
        // GIVEN
        underTest.addParagraph(character, "180");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394   or   (180 and 314)");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenTripleOrWithOneVisitedShouldReturnTrue() {
        // GIVEN
        underTest.addParagraph(character, "180");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "394   or   180    or   314");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testVisitedParagraphWhenTripleOrWithOneVisitedNegatedShouldReturnFalse() {
        // GIVEN
        underTest.addParagraph(character, "180");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "not (394   or   180    or   314)");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testVisitedParagraphWhenSingleNumberWithPartialMatchesShouldReturnFalse() {
        // GIVEN
        underTest.addParagraph(character, "20");
        underTest.addParagraph(character, "1");
        // WHEN
        final boolean returned = underTest.visitedParagraph(character, "201");
        // THEN
        Assert.assertFalse(returned);
    }

}
