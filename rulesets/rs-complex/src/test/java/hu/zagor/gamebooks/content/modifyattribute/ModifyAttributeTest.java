package hu.zagor.gamebooks.content.modifyattribute;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ModifyAttribute}.
 * @author Tamas_Szekeres
 */
@Test
public class ModifyAttributeTest {

    public void testConstructorWhenDefaultIsUsedShouldCreateEmptyBean() {
        // GIVEN
        // WHEN
        final ModifyAttribute returned = new ModifyAttribute();
        // THEN
        Assert.assertEquals(returned.getAttribute(), "");
        Assert.assertEquals(returned.getAmount(), 0);
    }

    public void testCloneShouldReturnObjectWithSameValues() throws CloneNotSupportedException {
        // GIVEN
        final ModifyAttribute underTest = new ModifyAttribute("skill", 3, ModifyAttributeType.change);
        // WHEN
        final ModifyAttribute returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
        Assert.assertEquals(returned.getAmount(), underTest.getAmount());
        Assert.assertEquals(returned.getAttribute(), underTest.getAttribute());
    }
}
