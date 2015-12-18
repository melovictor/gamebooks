package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.random.RandomResult;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link RandomResultTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomResultTransformerTest extends AbstractTransformerTest {

    private RandomResultTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;
    private RandomCommand command;
    private BeanFactory beanFactory;
    private ParagraphData paragraphData;
    private RandomResult randomResult;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        paragraphData = mockControl.createMock(ParagraphData.class);

        underTest = new RandomResultTransformer();
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        randomResult = new RandomResult();
        command = new RandomCommand();
    }

    public void testDoTransformShouldParseResultAttributesAndResultItself() {
        // GIVEN
        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult);
        expectAttribute("min", "1");
        expectAttribute("max", "3");
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getResults().get(0), randomResult);
        Assert.assertEquals(randomResult.getMin(), "1");
        Assert.assertEquals(randomResult.getMax(), "3");
        Assert.assertSame(randomResult.getParagraphData(), paragraphData);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
