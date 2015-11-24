package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;

import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultSectionTextUpdater}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultSectionTextUpdaterTest {

    @MockControl
    private IMocksControl mockControl;
    @UnderTest
    private DefaultSectionTextUpdater underTest;
    @Inject
    private PositionManipulator positionManipulator;
    @Mock
    private Paragraph paragraph;
    @Instance
    private HuntRoundResult result;
    @Mock
    private ParagraphData data;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testUpdateParagraphContentWhenNotFinishedShouldUpdateParagraphText() {
        // GIVEN
        result.setDogPosition("B10");
        result.setTigerPosition("F3");
        result.setRoundMessage("Tiger rolled 5.<br />");
        result.setHuntFinished(false);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getText()).andReturn(
            "<div id=\"ff23HuntProcession\">\r\n" + "<div id=\"ff23HuntMapContainer\">\r\n"
                + "<img id=\"ff23HuntMap\" src=\"resources/ff23/240.jpg?bwFirst\" /><img id=\"ff23Tiger\" src=\"resources/ff23/tiger.png?colFirst\" class=\"gE g4\" />"
                + "<img id=\"ff23Dog\" src=\"resources/ff23/dog.png?colFirst\" class=\"gE g12\" />\r\n" + "</div>\r\n"
                + "<div id=\"ff23HuntReport\"></div><button id=\"ff23HuntTrigger\">Roll</button>\r\n" + "</div>");
        expect(positionManipulator.cssClassFromPosition("F3")).andReturn("gF g3");
        expect(positionManipulator.cssClassFromPosition("B10")).andReturn("gB g10");
        data.setText("<div id=\"ff23HuntProcession\">\r\n" + "<div id=\"ff23HuntMapContainer\">\r\n"
            + "<img id=\"ff23HuntMap\" src=\"resources/ff23/240.jpg\" /><img id=\"ff23Tiger\" src=\"resources/ff23/tiger.png\" class=\"gF g3\" />"
            + "<img id=\"ff23Dog\" src=\"resources/ff23/dog.png\" class=\"gB g10\" />\r\n" + "</div>\r\n"
            + "<div id=\"ff23HuntReport\">Tiger rolled 5.<br /></div><button id=\"ff23HuntTrigger\">Roll</button>\r\n" + "</div>");
        mockControl.replay();
        // WHEN
        underTest.updateParagraphContent(paragraph, result);
        // THEN
    }

    public void testUpdateParagraphContentWhenFinishedShouldUpdateParagraphTextAndInsertHiddenSectionTrigger() {
        // GIVEN
        result.setDogPosition("G6");
        result.setTigerPosition("H5");
        result.setRoundMessage("Dog rolled 3.<br />Tiger caught.<br />");
        result.setHuntFinished(true);
        result.setNextSectionId("166");
        expect(paragraph.getData()).andReturn(data);
        expect(data.getText()).andReturn(
            "<div id=\"ff23HuntProcession\">\r\n" + "<div id=\"ff23HuntMapContainer\">\r\n"
                + "<img id=\"ff23HuntMap\" src=\"resources/ff23/240.jpg\" /><img id=\"ff23Tiger\" src=\"resources/ff23/tiger.png\" class=\"gF g3\" />"
                + "<img id=\"ff23Dog\" src=\"resources/ff23/dog.png\" class=\"gB g10\" />\r\n" + "</div>\r\n"
                + "<div id=\"ff23HuntReport\">Tiger rolled 5.<br /></div><button id=\"ff23HuntTrigger\">Roll</button>\r\n" + "</div>");
        expect(positionManipulator.cssClassFromPosition("H5")).andReturn("gH g5");
        expect(positionManipulator.cssClassFromPosition("G6")).andReturn("gG g6");
        data.setText("<div id=\"ff23HuntProcession\">\r\n" + "<div id=\"ff23HuntMapContainer\">\r\n"
            + "<img id=\"ff23HuntMap\" src=\"resources/ff23/240.jpg\" /><img id=\"ff23Tiger\" src=\"resources/ff23/tiger.png\" class=\"gH g5\" />"
            + "<img id=\"ff23Dog\" src=\"resources/ff23/dog.png\" class=\"gG g6\" />\r\n" + "</div>\r\n"
            + "<div id=\"ff23HuntReport\">Tiger rolled 5.<br />Dog rolled 3.<br />Tiger caught.<br /></div><button id=\"ff23HuntTrigger\">Roll</button>\r\n"
            + "</div><input type='hidden' id='ff23HuntFinished' value='166' />");
        mockControl.replay();
        // WHEN
        underTest.updateParagraphContent(paragraph, result);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
