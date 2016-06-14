package hu.zagor.gamebooks.renderer;

import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultDiceResultRenderer}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultDiceResultRendererTest {

    private DefaultDiceResultRenderer underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new DefaultDiceResultRenderer();
    }

    public void testRenderWhenSixSidedDiceWithSingleResultShouldReturnOneEntry() {
        // GIVEN
        final int[] results = new int[]{3, 3};
        // WHEN
        final String returned = underTest.render(6, results);
        // THEN
        Assert.assertEquals(returned, "<span class='diced63'></span>");
    }

    public void testRenderWhenSixSidedDiceConfigWithThreeResultsShouldReturnThreeEntries() {
        // GIVEN
        final int[] results = new int[]{7, 1, 4, 2};
        final DiceConfiguration config = new DiceConfiguration(3, 1, 6);
        // WHEN
        final String returned = underTest.render(config, results);
        // THEN
        Assert.assertEquals(returned, "<span class='diced61'></span><span class='diced64'></span><span class='diced62'></span>");
    }

}
