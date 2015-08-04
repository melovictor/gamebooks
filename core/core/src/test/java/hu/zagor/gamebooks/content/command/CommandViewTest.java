package hu.zagor.gamebooks.content.command;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CommandView}.
 * @author Tamas_Szekeres
 */
@Test
public class CommandViewTest {

    public void testConstructorShouldCreateEmptyObject() {
        // GIVEN
        // WHEN
        final CommandView underTest = new CommandView();
        // THEN
        Assert.assertNull(underTest.getViewName());
        Assert.assertNull(underTest.getModel());
    }

}
