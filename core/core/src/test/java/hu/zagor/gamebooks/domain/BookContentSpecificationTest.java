package hu.zagor.gamebooks.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BookContentSpecification}.
 * @author Tamas_Szekeres
 */
@Test
public class BookContentSpecificationTest {

    public void testGetCharacterBackpackSizeShouldReturnPreviouslySetValue() {
        // GIVEN
        final BookContentSpecification underTest = new BookContentSpecification();
        underTest.setCharacterBackpackSize(7);
        // WHEN
        final int returned = underTest.getCharacterBackpackSize();
        // THEN
        Assert.assertEquals(returned, 7);
    }

}
