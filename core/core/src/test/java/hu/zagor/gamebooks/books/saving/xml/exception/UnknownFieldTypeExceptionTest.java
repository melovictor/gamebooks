package hu.zagor.gamebooks.books.saving.xml.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UnknownFieldTypeException}.
 * @author Tamas_Szekeres
 */
@Test
public class UnknownFieldTypeExceptionTest {

    public void testGetMessageShouldReturnTextWithProblematicClassName() {
        // GIVEN
        final String problematicClassName = "java.lang.Shrot";
        final String message = "Couldn't unmarshall type '" + problematicClassName + "'.";
        // WHEN
        final String returned = new UnknownFieldTypeException(problematicClassName).getMessage();
        // THEN
        Assert.assertEquals(returned, message);
    }
}
