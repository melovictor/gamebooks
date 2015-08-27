package hu.zagor.gamebooks.raw.mvc.book.section.controller;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.recording.NavigationRecorder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class RawBookSectionControllerPositiveATest {

    private static final String DEFAULT_TEXT = "<p>This is some sample text.</p><p>This is text. <alt>This is to be replaced.</alt> End of text.</p>";
    private static final String REPLACED_TEXT = "<p>This is some sample text.</p><p>This is text. Extra text. End of text.</p>";

    private RawBookSectionController underTest;
    private IMocksControl mockControl;
    private SectionHandlingService sectionHandlingService;
    private Model model;
    private HttpServletRequest request;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private Paragraph oldParagraph;
    private BeanFactory beanFactory;
    private Logger logger;
    private BookInformations info;
    private CharacterHandler characterHandler;
    private Character character;
    private CharacterParagraphHandler paragraphHandler;
    private PlayerUser player;
    private BookParagraphResolver paragraphResolver;
    private Paragraph newParagraph;
    private RawCharacterPageData charPageData;
    private BookContentInitializer contentInitializer;
    private ParagraphData data;
    private ChoiceSet choices;
    private Choice choice;
    private ChoiceSet choicesMixed;
    private ChoiceSet choicesExtra;
    private Choice choiceWithExtra;
    private Map<String, Enemy> enemies;
    private NavigationRecorder navigationRecorder;
    private Map<String, Object> modelMap;
    private StaticResourceDescriptor staticResourceDescriptor;
    private Set<String> resourceSet;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandlingService = mockControl.createMock(SectionHandlingService.class);
        model = mockControl.createMock(Model.class);
        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        oldParagraph = new Paragraph("9", null, Integer.MAX_VALUE);
        beanFactory = mockControl.createMock(BeanFactory.class);
        logger = mockControl.createMock(Logger.class);
        info = new BookInformations(1L);
        characterHandler = new CharacterHandler();
        paragraphHandler = mockControl.createMock(CharacterParagraphHandler.class);
        characterHandler.setParagraphHandler(paragraphHandler);
        info.setCharacterHandler(characterHandler);
        character = new Character();
        player = new PlayerUser(3, "FireFoX", false);
        paragraphResolver = mockControl.createMock(BookParagraphResolver.class);
        info.setParagraphResolver(paragraphResolver);
        newParagraph = mockControl.createMock(Paragraph.class);
        charPageData = mockControl.createMock(RawCharacterPageData.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        player.getSettings().setImageTypeOrder("bwFirst");
        data = new ParagraphData();
        oldParagraph.setData(data);
        enemies = new HashMap<>();
        navigationRecorder = mockControl.createMock(NavigationRecorder.class);
        resourceSet = mockControl.createMock(Set.class);
        staticResourceDescriptor = mockControl.createMock(StaticResourceDescriptor.class);
        modelMap = mockControl.createMock(Map.class);

        underTest = new RawBookSectionController(sectionHandlingService);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "info", info);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        Whitebox.setInternalState(underTest, "navigationRecorder", navigationRecorder);
    }

    @BeforeMethod
    public void setUpMethod() {
        data.setText(DEFAULT_TEXT);
        choiceWithExtra = new Choice("10", null, 0, "Extra text.");
        choice = new Choice("9", null, 1, null);
        choicesMixed = new DefaultChoiceSet(new ChoicePositionComparator());
        choicesMixed.add(choice);
        choicesMixed.add(choiceWithExtra);
        choicesExtra = new DefaultChoiceSet(new ChoicePositionComparator());
        choicesExtra.add(choiceWithExtra);
        choices = new DefaultChoiceSet(new ChoicePositionComparator());
        choices.add(choice);
        data.setChoices(choices);
        mockControl.reset();
    }

    public void testHandleSectionWhenSectionIsWithIdShouldHandleSectionBasedOnSectionId() {
        // GIVEN
        prepareForSwitch("9");
        setUpNewParagraph();
        setUpModel();
        expect(newParagraph.getData()).andReturn(data);
        extractWrapper();
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(newParagraph));
        newParagraph.calculateValidEvents();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("rawCharacterPageData", character)).andReturn(charPageData);
        expect(model.addAttribute("charEquipments", charPageData)).andReturn(model);
        expect(sectionHandlingService.handleSection(model, wrapper, newParagraph, info)).andReturn("done");
        expect(newParagraph.getData()).andReturn(data);
        expect(newParagraph.getData()).andReturn(data);
        expect(sectionHandlingService.resolveParagraphId(info, "9")).andReturn("9");
        expect(wrapper.setModel(model)).andReturn(model);
        navigationRecorder.recordNavigation(wrapper, "s-9", oldParagraph, newParagraph);
        expectResources();
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleSection(model, request, "s-9");
        // THEN
        Assert.assertEquals(returned, "done");
    }

    public void testHandleSectionWhenSectionIsWithChoicePositionShouldHandleSectionBasedOnPosition() {
        // GIVEN
        logger.debug("Handling choice request '{}' for book.", "1");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(wrapper.getPlayer()).andReturn(player);
        logger.debug("Handling paragraph {} for book.", "9");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(contentInitializer.loadSection("9", player, oldParagraph, info)).andReturn(newParagraph);
        expect(wrapper.setParagraph(newParagraph)).andReturn(newParagraph);
        expect(wrapper.getCharacter()).andReturn(character);
        setUpNewParagraph();
        setUpModel();
        expect(newParagraph.getData()).andReturn(data);
        extractWrapper();
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(newParagraph));
        newParagraph.calculateValidEvents();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("rawCharacterPageData", character)).andReturn(charPageData);
        expect(model.addAttribute("charEquipments", charPageData)).andReturn(model);
        expect(sectionHandlingService.handleSection(model, wrapper, newParagraph, info)).andReturn("done");
        expect(newParagraph.getData()).andReturn(data);
        expect(newParagraph.getData()).andReturn(data);
        expect(sectionHandlingService.resolveParagraphId(info, "9")).andReturn("9");
        expect(wrapper.setModel(model)).andReturn(model);
        navigationRecorder.recordNavigation(wrapper, "1", oldParagraph, newParagraph);
        expectResources();
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleSection(model, request, "1");
        // THEN
        Assert.assertEquals(returned, "done");
    }

    public void testHandleSectionWhenSingleChoiceIsLeftWithSingleChoiceTextShouldIncorporateExtraTextIntoMainBlock() {
        // GIVEN
        data.setChoices(choicesExtra);
        prepareForSwitch("10");
        setUpNewParagraph();
        setUpModel();
        expect(newParagraph.getData()).andReturn(data);
        extractWrapper();
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(newParagraph));
        newParagraph.calculateValidEvents();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("rawCharacterPageData", character)).andReturn(charPageData);
        expect(model.addAttribute("charEquipments", charPageData)).andReturn(model);
        expect(sectionHandlingService.handleSection(model, wrapper, newParagraph, info)).andReturn("done");
        expect(newParagraph.getData()).andReturn(data);
        expect(newParagraph.getData()).andReturn(data);
        expect(sectionHandlingService.resolveParagraphId(info, "10")).andReturn("10");
        expect(wrapper.setModel(model)).andReturn(model);
        navigationRecorder.recordNavigation(wrapper, "s-10", oldParagraph, newParagraph);
        expectResources();
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleSection(model, request, "s-10");
        // THEN
        Assert.assertEquals(data.getText(), REPLACED_TEXT);
        Assert.assertEquals(returned, "done");
    }

    private void expectResources() {
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(staticResourceDescriptor);
        expect(model.addAttribute("resources", staticResourceDescriptor)).andReturn(model);
        expect(staticResourceDescriptor.getJs()).andReturn(resourceSet);
        expect(resourceSet.add("raw.js")).andReturn(true);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(staticResourceDescriptor);
        expect(staticResourceDescriptor.getCss()).andReturn(resourceSet);
        expect(resourceSet.add("raw.css")).andReturn(true);
    }

    private void prepareForSwitch(final String id) {
        logger.debug("Handling choice request '{}' for book.", "s-" + id);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(wrapper.getPlayer()).andReturn(player);
        logger.debug("Handling paragraph {} for book.", id);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(contentInitializer.loadSection(id, player, oldParagraph, info)).andReturn(newParagraph);
        expect(wrapper.setParagraph(newParagraph)).andReturn(newParagraph);
        expect(wrapper.getCharacter()).andReturn(character);
    }

    private void setUpNewParagraph() {
        expect(newParagraph.getId()).andReturn("10");
        paragraphHandler.addParagraph(character, "10");
        expect(wrapper.getPlayer()).andReturn(player);
        expect(newParagraph.getId()).andReturn("10");
    }

    private void setUpModel() {
        expect(model.addAttribute("hideTopSection", true)).andReturn(model);
        expect(model.addAttribute("hideChoiceSection", true)).andReturn(model);
        expect(model.addAttribute("imgTypeOrder", "bwFirst")).andReturn(model);
        expect(model.addAttribute("informativeSections", false)).andReturn(model);
        expect(model.addAttribute("bookInfo", info)).andReturn(model);
    }

    private void extractWrapper() {
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getPlayer()).andReturn(player);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
