package hu.zagor.gamebooks.mvc.book.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.ResourceInformation;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AbstractSectionDisplayingController}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractSectionDisplayingControllerTest {
    private AbstractSectionDisplayingController underTest;
    @Mock private Model model;
    @MockControl private IMocksControl mockControl;
    @Mock private Map<String, Object> modelMap;
    @Inject private BeanFactory beanFactory;
    @Instance private StaticResourceDescriptor descriptor;
    private BookInformations info;
    @Instance private ResourceInformation emptyResourceInfo;
    @Instance private ResourceInformation filledResourceInfo;
    @Inject private ApplicationContext applicationContext;
    @Mock private Paragraph newParagraph;
    @Instance private ParagraphData data;

    @UnderTest
    public AbstractSectionDisplayingController underTest() {
        return new AbstractSectionDisplayingController() {
        };
    }

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        info.setResourceDir("book3");
        Whitebox.setInternalState(underTest, "info", info);
        filledResourceInfo.setCssResources("raw,ff,ff15");
        filledResourceInfo.setJsResources("raw,ff,ff15");
    }

    @BeforeMethod
    public void setUpMethod() {
        info.setResources(emptyResourceInfo);
    }

    public void testSetUpSectionDisplayOptionsWhenParagraphIsBackgroundShouldConfigureModelProperlyWithoutSettingHideTopSection() {
        // GIVEN
        final Model model = new ExtendedModelMap();
        final Paragraph paragraph = new Paragraph(BookParagraphConstants.BACKGROUND.getValue(), "background", Integer.MAX_VALUE);
        final PlayerUser player = new PlayerUser(11, "FireFoX", false);
        final PlayerSettings settings = player.getSettings();
        settings.setChoiceSectionDisplayable(true);
        settings.setImageTypeOrder("bwFirst");
        settings.setInformativeSectionsRequested(true);
        mockControl.replay();
        // WHEN
        underTest.setUpSectionDisplayOptions(paragraph, model, player);
        // THEN
        final Map<String, Object> modelMap = model.asMap();
        Assert.assertFalse(modelMap.containsKey("hideTopSection"));
        Assert.assertEquals(modelMap.get("hideChoiceSection"), false);
        Assert.assertEquals(modelMap.get("imgTypeOrder"), "bwFirst");
        Assert.assertEquals(modelMap.get("informativeSections"), true);
    }

    public void testSetUpSectionDisplayOptionsWhenParagraphIsNotBackgroundAndTopSectionIsNotDisplayableShouldConfigureModelProperlyWithSettingHideTopSection() {
        // GIVEN
        final Model model = new ExtendedModelMap();
        final Paragraph paragraph = new Paragraph("9", null, Integer.MAX_VALUE);
        final PlayerUser player = new PlayerUser(11, "FireFoX", false);
        final PlayerSettings settings = player.getSettings();
        settings.setTopSectionDisplayable(false);
        settings.setChoiceSectionDisplayable(false);
        settings.setImageTypeOrder("colFirst");
        settings.setInformativeSectionsRequested(false);
        mockControl.replay();
        // WHEN
        underTest.setUpSectionDisplayOptions(paragraph, model, player);
        // THEN
        final Map<String, Object> modelMap = model.asMap();
        Assert.assertEquals(modelMap.get("hideTopSection"), true);
        Assert.assertEquals(modelMap.get("hideChoiceSection"), true);
        Assert.assertEquals(modelMap.get("imgTypeOrder"), "colFirst");
        Assert.assertEquals(modelMap.get("informativeSections"), false);
    }

    public void testSetUpSectionDisplayOptionsWhenParagraphIsNotBackgroundAndTopSectionIsDisplayableShouldConfigureModelProperlyWithSettingHideTopSection() {
        // GIVEN
        final Model model = new ExtendedModelMap();
        final Paragraph paragraph = new Paragraph("9", null, Integer.MAX_VALUE);
        final PlayerUser player = new PlayerUser(11, "FireFoX", false);
        final PlayerSettings settings = player.getSettings();
        settings.setTopSectionDisplayable(true);
        settings.setChoiceSectionDisplayable(false);
        settings.setImageTypeOrder("colFirst");
        settings.setInformativeSectionsRequested(false);
        mockControl.replay();
        // WHEN
        underTest.setUpSectionDisplayOptions(paragraph, model, player);
        // THEN
        final Map<String, Object> modelMap = model.asMap();
        Assert.assertEquals(modelMap.get("hideTopSection"), false);
        Assert.assertEquals(modelMap.get("hideChoiceSection"), true);
        Assert.assertEquals(modelMap.get("imgTypeOrder"), "colFirst");
        Assert.assertEquals(modelMap.get("informativeSections"), false);
    }

    public void testAddJsResourceWhenInfoContainsNullResourcesShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        info.setResources(null);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(descriptor);
        expect(modelMap.put("resources", descriptor)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.addJsResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getJs().contains("bze.js"));
        Assert.assertEquals(descriptor.getJs().size(), 1);
    }

    public void testAddCssResourceWhenInfoContainsNullResourcesShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        info.setResources(null);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(descriptor);
        mockControl.replay();
        // WHEN
        underTest.addCssResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getCss().contains("bze.css"));
        Assert.assertEquals(descriptor.getCss().size(), 1);
    }

    public void testAddJsResourceWhenInfoContainsNoResourcesShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(descriptor);
        expect(modelMap.put("resources", descriptor)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.addJsResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getJs().contains("bze.js"));
        Assert.assertEquals(descriptor.getJs().size(), 1);
    }

    public void testAddCssResourceWhenInfoContainsNoResourcesShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(descriptor);
        mockControl.replay();
        // WHEN
        underTest.addCssResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getCss().contains("bze.css"));
        Assert.assertEquals(descriptor.getCss().size(), 1);
    }

    public void testAddJsResourceWhenInfoContainsResourcesShouldAddResourcesToStaticResourceDescriptor() {
        // GIVEN
        info.setResources(filledResourceInfo);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(descriptor);
        expect(modelMap.put("resources", descriptor)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.addJsResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getJs().contains("bze.js"));
        Assert.assertTrue(descriptor.getJs().contains("raw.js"));
        Assert.assertTrue(descriptor.getJs().contains("ff.js"));
        Assert.assertTrue(descriptor.getJs().contains("ff15.js"));
        Assert.assertEquals(descriptor.getJs().size(), 4);
    }

    public void testAddCssResourceWhenInfoContainsResourcesShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        info.setResources(filledResourceInfo);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(descriptor);
        mockControl.replay();
        // WHEN
        underTest.addCssResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getCss().contains("bze.css"));
        Assert.assertTrue(descriptor.getCss().contains("raw.css"));
        Assert.assertTrue(descriptor.getCss().contains("ff.css"));
        Assert.assertTrue(descriptor.getCss().contains("ff15.css"));
        Assert.assertEquals(descriptor.getCss().size(), 4);
    }

    public void testGetApplicationContextShouldReturnApplicationContext() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final ApplicationContext returned = underTest.getApplicationContext();
        // THEN
        Assert.assertSame(returned, applicationContext);
    }

    public void testMarkParagraphImagesWhenCoreImageShouldLeaveItAlone() {
        // GIVEN
        expect(newParagraph.getData()).andReturn(data);
        data.setText("sample text with an image: <img src=\"../resources/dice.jpg\" />");
        mockControl.replay();
        // WHEN
        underTest.markParagraphImages(newParagraph, "colFirst");
        // THEN
        Assert.assertEquals(data.getText(), "sample text with an image: <img src=\"../resources/dice.jpg?colFirst\" />");
    }

    public void testMarkParagraphImagesWhenUserNeedsBwImageShouldRewriteImageInTextWithBwQuery() {
        // GIVEN
        expect(newParagraph.getData()).andReturn(data);
        data.setText("sample text with an image: <img src=\"resources/book1/99.jpg\" />" + "sample text with an image: <img src=\"../resources/99.jpg\" />"
            + "sample text with an image: <img src=\"resources/book2/99.png\" />" + "sample text with an image: <p class=\"inlineImage\" data-img=\"intro\"></p>");
        mockControl.replay();
        // WHEN
        underTest.markParagraphImages(newParagraph, "bwFirst");
        // THEN
        Assert.assertTrue(data.getText().contains("sample text with an image: <img src=\"resources/book1/99.jpg?bwFirst\" />"));
        Assert.assertTrue(data.getText().contains("sample text with an image: <img src=\"../resources/99.jpg?bwFirst\" />"));
        Assert.assertTrue(data.getText().contains("sample text with an image: <img src=\"resources/book2/99.png?bwFirst\" />"));
        Assert.assertTrue(data.getText().contains("sample text with an image: <p class=\"inlineImage\" data-book=\"book3\" data-type=\"b\" data-img=\"intro\"></p>"));
    }

    public void testMarkParagraphImagesWhenUserImagesRewrittenOnceShouldLeaveThemAlone() {
        // GIVEN
        expect(newParagraph.getData()).andReturn(data);
        final String originalText = "sample text with an image: <img src=\"resources/book1/99.jpg?bwFirst\" />"
            + "sample text with an image: <img src=\"../resources/99.jpg?bwFirst\" />" + "sample text with an image: <img src=\"resources/book2/99.png?bwFirst\" />"
            + "sample text with an image: <p class=\"inlineImage\" data-book=\"book3\" data-type=\"b\" data-img=\"intro\"></p>";
        data.setText(originalText);
        mockControl.replay();
        // WHEN
        underTest.markParagraphImages(newParagraph, "bwFirst");
        // THEN
        Assert.assertEquals(data.getText(), originalText);
    }

    public void testMarkParagraphImagesWhenUserNeedsColorImageShouldRewriteImageInTextWithColorQuery() {
        // GIVEN
        expect(newParagraph.getData()).andReturn(data);
        data.setText("sample text with an image: <img src=\"resources/book1/99.jpg\" />" + "sample text with an image: <img src=\"../resources/99.jpg\" />"
            + "sample text with an image: <img src=\"resources/book2/99.png\" />" + "sample text with an image: <p class=\"inlineImage\" data-img=\"intro\"></p>");
        mockControl.replay();
        // WHEN
        underTest.markParagraphImages(newParagraph, "colFirst");
        // THEN
        Assert.assertTrue(data.getText().contains("sample text with an image: <img src=\"resources/book1/99.jpg?colFirst\" />"));
        Assert.assertTrue(data.getText().contains("sample text with an image: <img src=\"../resources/99.jpg?colFirst\" />"));
        Assert.assertTrue(data.getText().contains("sample text with an image: <img src=\"resources/book2/99.png?colFirst\" />"));
        Assert.assertTrue(data.getText().contains("sample text with an image: <p class=\"inlineImage\" data-book=\"book3\" data-type=\"c\" data-img=\"intro\"></p>"));
    }

    public void testMarkParagraphImagesWhenRulesetImagesShouldLeaveAlone() {
        // GIVEN
        expect(newParagraph.getData()).andReturn(data);
        data.setText("sample text with an image: <img src=\"resources/ff/dice.jpg\" />");
        mockControl.replay();
        // WHEN
        underTest.markParagraphImages(newParagraph, "colFirst");
        // THEN
        Assert.assertEquals(data.getText(), "sample text with an image: <img src=\"resources/ff/dice.jpg?colFirst\" />");
    }

}
