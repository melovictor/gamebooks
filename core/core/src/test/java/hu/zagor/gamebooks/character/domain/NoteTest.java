package hu.zagor.gamebooks.character.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Note}.
 * @author Tamas_Szekeres
 */
@Test
public class NoteTest {
    private final Note underTest = new Note();

    public void testCloneShouldReturnClonedObject() throws CloneNotSupportedException {
        // GIVEN
        underTest.setNote("apple");
        // WHEN
        final Note returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
        Assert.assertEquals(returned.getNote(), underTest.getNote());
    }

}
