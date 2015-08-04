package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

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
 * Unit test for class {@link ChoiceTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ChoiceTransformerTest extends AbstractTransformerTest {

    private static final int POSITION = 9;
    private static final String POSITION_STRING = "9";
    private static final String ID = "10";
    private static final String TEXT = "Text";
    private static final String ALTERNATE_SCC = "Alt text.";
    private ChoiceTransformer underTest;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;
    private Choice choice;
    private IMocksControl mockControl;
    private ChoicePositionCounter positionCounter;
    private BeanFactory beanFactory;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        data = mockControl.createMock(ParagraphData.class);
        choice = mockControl.createMock(Choice.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ChoiceTransformer();
        underTest.setBeanFactory(beanFactory);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenParentIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(null, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, null, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenDataIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, null);
        // THEN throws exception
    }

    public void testTransformWhenNodeContainsNoPositionAndTextShouldRequestPositionParameterFromPositionCounterAndHaveDefaultText() throws Exception {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);

        expectAttribute("id", ID);
        expectContent();
        expectAttribute("pos");
        expect(positionCounter.updateAndGetPosition(null)).andReturn(POSITION);
        expectAttribute("singleChoiceText");
        expect(beanFactory.getBean("choice", ID, null, POSITION, null)).andReturn(choice);
        data.addChoice(choice);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenNodeContainsNoTextShouldUpdatePositionParameterInPositionCounterAndHaveDefaultText() throws Exception {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);

        expectAttribute("id", ID);
        expectContent();
        expectAttribute("pos", POSITION_STRING);
        expect(positionCounter.updateAndGetPosition(POSITION)).andReturn(POSITION);
        expectAttribute("singleChoiceText");
        expect(beanFactory.getBean("choice", ID, null, POSITION, null)).andReturn(choice);
        data.addChoice(choice);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenNodeContainsNoPositionShouldRequestPositionParameterFromPositionCounter() throws Exception {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expectAttribute("id", ID);
        expectContent(TEXT);
        expectAttribute("pos");
        expect(positionCounter.updateAndGetPosition(null)).andReturn(POSITION);
        expectAttribute("singleChoiceText");
        expect(beanFactory.getBean("choice", ID, TEXT, POSITION, null)).andReturn(choice);
        data.addChoice(choice);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenNodeContainsEverythingShouldUpdatePositionParameterInPositionCounter() throws Exception {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);

        expectAttribute("id", ID);
        expectContent(TEXT);
        expectAttribute("pos", POSITION_STRING);
        expect(positionCounter.updateAndGetPosition(POSITION)).andReturn(POSITION);
        expectAttribute("singleChoiceText", ALTERNATE_SCC);
        expect(beanFactory.getBean("choice", ID, TEXT, POSITION, ALTERNATE_SCC)).andReturn(choice);
        data.addChoice(choice);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
