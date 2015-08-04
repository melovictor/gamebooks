package hu.zagor.gamebooks.books.contentransforming.section.stub;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link StubTransformerMerger}.
 * @author Tamas_Szekeres
 */
@Test
public class StubTransformerMergerTest {

    private IMocksControl mockControl;
    private StubTransformerMerger underTest;
    private final Map<String, StubTransformer> common = new HashMap<>();
    private final Map<String, StubTransformer> nonOverriding = new HashMap<>();
    private final Map<String, StubTransformer> overriding = new HashMap<>();
    private StubTransformer transformerA;
    private StubTransformer transformerB;
    private StubTransformer transformerC;
    private StubTransformer transformerD;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new StubTransformerMerger();

        transformerA = mockControl.createMock(StubTransformer.class);
        transformerB = mockControl.createMock(StubTransformer.class);
        transformerC = mockControl.createMock(StubTransformer.class);
        transformerD = mockControl.createMock(StubTransformer.class);

        common.put("a", transformerA);
        common.put("b", transformerB);
        nonOverriding.put("c", transformerC);
        overriding.put("a", transformerD);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWhenFirstIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.merge(null, common);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWhenSecondIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.merge(common, null);
        // THEN throws exception
    }

    public void testMergeWhenNoOverridingOccursShouldReturnMergedMap() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Map<String, StubTransformer> returned = underTest.merge(common, nonOverriding);
        // THEN
        Assert.assertEquals(returned.size(), 3);
        Assert.assertSame(returned.get("a"), transformerA);
        Assert.assertSame(returned.get("b"), transformerB);
        Assert.assertSame(returned.get("c"), transformerC);
    }

    public void testMergeWhenOverridingOccursShouldReturnMergedOverridenMap() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Map<String, StubTransformer> returned = underTest.merge(common, overriding);
        // THEN
        Assert.assertEquals(returned.size(), 2);
        Assert.assertSame(returned.get("a"), transformerD);
        Assert.assertSame(returned.get("b"), transformerB);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
