package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CodewordTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class CodewordTransformerTest extends AbstractTransformerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private CodewordTransformer underTest;
    @Mock private BookParagraphDataTransformer parent;
    @Instance private ParagraphData data;

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDoTransformWhenParentIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.doTransform(null, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDoTransformWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, null, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDoTransformWhenDataIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, null);
        // THEN throws exception
    }

    public void testDoTransformWhenParamsAreAvailableShouldExtractAndAddCodeword() {
        // GIVEN
        expectAttribute("name", "ship");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertTrue(data.getCodewords().contains("ship"));
    }
}
