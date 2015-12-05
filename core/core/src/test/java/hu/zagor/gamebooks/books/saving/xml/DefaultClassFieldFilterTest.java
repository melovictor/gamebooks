package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.domain.IgnoreField;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultClassFieldFilter}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultClassFieldFilterTest {

    private DefaultClassFieldFilter underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new DefaultClassFieldFilter();
    }

    public void testIsIgnorableFieldWhenMarkedFiledHasIgnoreFieldAnnotationShouldReturnTrue() {
        // GIVEN in setup
        // WHEN
        final boolean returned = underTest.isIgnorableField(new TestClass(), "safeToIgnore");
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsIgnorableFieldWhenMarkedFiledHasNoIgnoreFieldAnnotationShouldReturnFalse() {
        // GIVEN in setup
        // WHEN
        final boolean returned = underTest.isIgnorableField(new TestClass(), "normal");
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsIgnorableFieldWhenClassHasNoIgnoreFieldAnnotationShouldReturnFalse() {
        // GIVEN in setup
        // WHEN
        final boolean returned = underTest.isIgnorableField(new OtherTestClass(), "normal");
        // THEN
        Assert.assertFalse(returned);
    }

    @IgnoreField("safeToIgnore")
    class TestClass {

        private int normal;

        public int getNormal() {
            return normal;
        }

        public void setNormal(final int normal) {
            this.normal = normal;
        }
    }

    class OtherTestClass {

        private int normal;

        public int getNormal() {
            return normal;
        }

        public void setNormal(final int normal) {
            this.normal = normal;
        }
    }
}
