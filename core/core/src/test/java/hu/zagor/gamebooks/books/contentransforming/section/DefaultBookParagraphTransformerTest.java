package hu.zagor.gamebooks.books.contentransforming.section;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link DefaultBookParagraphTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultBookParagraphTransformerTest extends AbstractTransformerTest {

    private static final String PARAGRAPH_ID = "1";
    private static final String DISPLAY_ID = "Beginning";
    private DefaultBookParagraphTransformer underTest;
    private IMocksControl mockControl;
    private ParagraphData paragraphData;
    private BeanFactory beanFactory;
    private Logger logger;
    private List<String> irrelevantNodeNames;
    private Document document;
    private ChoicePositionCounter positionCounter;
    private Paragraph paragraph;
    private BookParagraphDataTransformer paragraphDataTransformer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        paragraphData = mockControl.createMock(ParagraphData.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        logger = mockControl.createMock(Logger.class);
        document = mockControl.createMock(Document.class);
        nodeList = mockControl.createMock(NodeList.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        node = mockControl.createMock(Node.class);
        paragraph = mockControl.createMock(Paragraph.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        paragraphDataTransformer = mockControl.createMock(BookParagraphDataTransformer.class);

        irrelevantNodeNames = new ArrayList<>();
        irrelevantNodeNames.add("#text");
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultBookParagraphTransformer();
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setParagraphDataTransformer(paragraphDataTransformer);
        final Set<String> validTags = new HashSet<String>();
        validTags.add("p");
        validTags.add("s");
        validTags.add("section");
        underTest.setValidSectionTags(validTags);

        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformParagraphsWhenDocumentIsNullShouldThrowException() throws XmlTransformationException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transformParagraphs(null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = XmlTransformationException.class)
    public void testTransformParagraphsWhenDocumentContainsNoChildNodesShouldThrowException() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        logger.error("Couldn't find 'content' element in xml file!");
        mockControl.replay();
        // WHEN
        underTest.transformParagraphs(document).getClass();
        // THEN throws exception
    }

    public void testTransformParagraphsWhenDocumentContainsNoPNodeShouldReturnEmptyMap() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);

        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");

        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("content");

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        mockControl.replay();
        // WHEN
        final Map<String, Paragraph> returned = underTest.transformParagraphs(document);
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    public void testTransformParagraphsWhenDocumentContainsPNodeShouldReturnFilledMap() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);

        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("content");

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("p");

        expectParagraphParsing();

        mockControl.replay();
        // WHEN
        final Map<String, Paragraph> returned = underTest.transformParagraphs(document);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertTrue(returned.containsKey(PARAGRAPH_ID));
        Assert.assertSame(returned.get(PARAGRAPH_ID), paragraph);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testTransformParagraphWhenParsingParagraphIdTwiceShouldThrowException() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("content");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(4);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("p");

        expectParagraphParsing();

        expect(nodeList.item(2)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        expect(nodeList.item(3)).andReturn(node);
        expect(node.getNodeName()).andReturn("p");

        expectParagraphParsing();

        mockControl.replay();
        // WHEN
        underTest.transformParagraphs(document);
        // THEN throws exception
    }

    private void expectParagraphParsing() {
        expectAttribute("id", PARAGRAPH_ID);
        expectAttribute("display", DISPLAY_ID);
        expectAttribute("actions");
        expect(beanFactory.getBean("paragraph", PARAGRAPH_ID, DISPLAY_ID, Integer.MAX_VALUE)).andReturn(paragraph);
        expect(paragraph.getPositionCounter()).andReturn(positionCounter);
        expect(paragraphDataTransformer.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        paragraph.setData(paragraphData);
        expect(paragraph.getId()).andReturn(PARAGRAPH_ID);
        logger.trace("Successfully parsed paragraph '{}'.", PARAGRAPH_ID);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
