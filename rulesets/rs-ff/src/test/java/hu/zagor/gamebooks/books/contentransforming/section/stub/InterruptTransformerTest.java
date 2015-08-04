package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link InterruptTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class InterruptTransformerTest {

    private final InterruptTransformer underTest = new InterruptTransformer();

    public void testDoTransformShouldSetDataInterruptFieldToTrue() {
        // GIVEN
        final BookParagraphDataTransformer parent = EasyMock.createMock(BookParagraphDataTransformer.class);
        final Node node = EasyMock.createMock(Node.class);
        final FfParagraphData data = new FfParagraphData();
        data.setInterrupt(false);
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertTrue(data.isInterrupt());
    }

}
