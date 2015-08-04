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
 * Unit test for class {@link RandomEvenOddTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomEvenOddTransformerTest extends AbstractTransformerTest {

    private RandomEvenOddTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;
    private RandomCommand command;
    private BeanFactory beanFactory;
    private ParagraphData paragraphData;
    private RandomResult randomResult1;
    private RandomResult randomResult3;
    private RandomResult randomResult5;

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
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        randomResult1 = new RandomResult();
        randomResult3 = new RandomResult();
        randomResult5 = new RandomResult();
        command = new RandomCommand();
    }

    public void testDoTransformWhenRemainderIsOddShouldParseResultAttributesAndResultItself() {
        // GIVEN
        underTest = new RandomEvenOddTransformer(1);
        underTest.setBeanFactory(beanFactory);

        expectAttribute("min", "1");
        expectAttribute("max", "6");
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);

        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult1);
        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult3);
        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult5);

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getResults().get(0), randomResult1);
        Assert.assertEquals(randomResult1.getMin(), 1);
        Assert.assertEquals(randomResult1.getMax(), 1);
        Assert.assertSame(randomResult1.getParagraphData(), paragraphData);

        Assert.assertSame(command.getResults().get(1), randomResult3);
        Assert.assertEquals(randomResult3.getMin(), 3);
        Assert.assertEquals(randomResult3.getMax(), 3);
        Assert.assertSame(randomResult3.getParagraphData(), paragraphData);

        Assert.assertSame(command.getResults().get(2), randomResult5);
        Assert.assertEquals(randomResult5.getMin(), 5);
        Assert.assertEquals(randomResult5.getMax(), 5);
        Assert.assertSame(randomResult5.getParagraphData(), paragraphData);
    }

    public void testDoTransformWhenRemainderIsEvenShouldParseResultAttributesAndResultItself() {
        // GIVEN
        underTest = new RandomEvenOddTransformer(0);
        underTest.setBeanFactory(beanFactory);

        expectAttribute("min", "1");
        expectAttribute("max", "6");
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);

        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult1);
        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult3);
        expect(beanFactory.getBean(RandomResult.class)).andReturn(randomResult5);

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getResults().get(0), randomResult1);
        Assert.assertEquals(randomResult1.getMin(), 2);
        Assert.assertEquals(randomResult1.getMax(), 2);
        Assert.assertSame(randomResult1.getParagraphData(), paragraphData);

        Assert.assertSame(command.getResults().get(1), randomResult3);
        Assert.assertEquals(randomResult3.getMin(), 4);
        Assert.assertEquals(randomResult3.getMax(), 4);
        Assert.assertSame(randomResult3.getParagraphData(), paragraphData);

        Assert.assertSame(command.getResults().get(2), randomResult5);
        Assert.assertEquals(randomResult5.getMin(), 6);
        Assert.assertEquals(randomResult5.getMax(), 6);
        Assert.assertSame(randomResult5.getParagraphData(), paragraphData);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
