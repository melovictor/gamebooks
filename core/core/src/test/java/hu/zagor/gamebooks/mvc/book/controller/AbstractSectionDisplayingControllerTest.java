package hu.zagor.gamebooks.mvc.book.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AbstractSectionDisplayingController}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractSectionDisplayingControllerTest {

    private final TestSectionDisplayingController underTest = new TestSectionDisplayingController();
    private Model model;
    private IMocksControl mockControl;
    private Map<String, Object> modelMap;
    private BeanFactory beanFactory;
    private StaticResourceDescriptor descriptor;
    private BookInformations info;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        model = mockControl.createMock(Model.class);
        modelMap = mockControl.createMock(Map.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        descriptor = new StaticResourceDescriptor();
        info = new BookInformations(1L);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
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

    public void testAddJsResourceShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(descriptor);
        expect(model.addAttribute("resources", descriptor)).andReturn(model);
        mockControl.replay();
        // WHEN
        underTest.addJsResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getJs().contains("bze.js"));
    }

    public void testAddCssResourceShouldAddResourceToStaticResourceDescriptor() {
        // GIVEN
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(descriptor);
        mockControl.replay();
        // WHEN
        underTest.addCssResource(model, "bze");
        // THEN
        Assert.assertTrue(descriptor.getCss().contains("bze.css"));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestSectionDisplayingController extends AbstractSectionDisplayingController {

    }
}
