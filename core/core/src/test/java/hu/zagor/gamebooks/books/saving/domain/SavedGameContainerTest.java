package hu.zagor.gamebooks.books.saving.domain;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SavedGameContainer}.
 * @author Tamas_Szekeres
 */
@Test
public class SavedGameContainerTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private SavedGameContainer underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new SavedGameContainer(10, 20L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetElementWhenKeyIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.getElement(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddElementWhenKeyIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.addElement(null, VALUE);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddElementWhenValueIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.addElement(KEY, null);
        // THEN throws exception
    }

    public void testGetElementWhenKeyIsInsideContainerShouldReturnValue() {
        // GIVEN
        underTest.addElement(KEY, VALUE);
        // WHEN
        final Object returned = underTest.getElement(KEY);
        // THEN
        Assert.assertSame(returned, VALUE);
    }

    public void testGetElementWhenKeyIsNotInsideContainerShouldReturnNull() {
        // GIVEN
        // WHEN
        final Object returned = underTest.getElement(KEY);
        // THEN
        Assert.assertNull(returned);
    }

}
