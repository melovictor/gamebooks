package hu.zagor.gamebooks.content.command.changeenemy;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ChangeEnemyCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class ChangeEnemyCommandTest {

    private ChangeEnemyCommand underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ChangeEnemyCommand();
    }

    public void testGetValidMoveShouldReturnNull() {
        // GIVEN
        // WHEN
        final String returned = underTest.getValidMove();
        // THEN
        Assert.assertNull(returned);
    }

    public void testToStringShouldReturnDescriptiveString() {
        // GIVEN
        underTest.setId("26");
        underTest.setAttribute("stamina");
        underTest.setNewValue(11);
        // WHEN
        final String returned = underTest.toString();
        // THEN
        Assert.assertEquals(returned, "ChangeEnemyCommand: 26 stamina (11 / null)");
    }

    public void testCloneShouldReturnClonedObject() throws CloneNotSupportedException {
        // GIVEN
        // WHEN
        final ChangeEnemyCommand returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
    }

}
