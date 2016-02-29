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
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.recording.NavigationRecorder;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
 * Unit test for class {@link RawBookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class RawBookSectionControllerPositiveBTest {

    private static final String DEFAULT_TEXT = "<p>This is some sample text.</p><p>This is text. <alt>This is to be replaced.</alt> End of text.</p>";
    private static final String DEFAULT_TEXT_WITHOUT_ALT = "<p>This is some sample text.</p><p>This is text. This is to be replaced. End of text.</p>";

    private RawBookSectionController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private SectionHandlingService sectionHandlingService;
    @Mock private Model model;
    @Mock private HttpServletRequest request;
    @Mock private HttpSessionWrapper wrapper;
    private Paragraph oldParagraph;
    @Inject private BeanFactory beanFactory;
    @Inject private Logger logger;
    private BookInformations info;
    @Instance private CharacterHandler characterHandler;
    @Instance private Character character;
    @Mock private CharacterParagraphHandler paragraphHandler;
    private PlayerUser player;
    @Mock private BookParagraphResolver paragraphResolver;
    @Mock private Paragraph newParagraph;
    @Mock private RawCharacterPageData charPageData;
    @Inject private BookContentInitializer contentInitializer;
    @Instance private ParagraphData data;
    private ChoiceSet choices;
    private Choice choice;
    private ChoiceSet choicesMixed;
    private ChoiceSet choicesExtra;
    private Choice choiceWithExtra;
    @Instance private Map<String, Enemy> enemies;
    @Inject private NavigationRecorder navigationRecorder;
    @Mock private Map<String, Object> modelMap;
    @Mock private StaticResourceDescriptor staticResourceDescriptor;
    @Mock private Set<String> resourceSet;
    @Instance(inject = true) private Map<String, CustomPrePostSectionHandler> prePostHandlers;

    @BeforeClass
    public void setUpClass() {
        oldParagraph = new Paragraph("9", null, Integer.MAX_VALUE);
        info = new BookInformations(1L);
        characterHandler.setParagraphHandler(paragraphHandler);
        info.setCharacterHandler(characterHandler);
        player = new PlayerUser(3, "FireFoX", false);
        info.setParagraphResolver(paragraphResolver);
        player.getSettings().setImageTypeOrder("bwFirst");
        oldParagraph.setData(data);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @UnderTest
    public RawBookSectionController underTest() {
        return new RawBookSectionController(sectionHandlingService);
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

    public void testHandleSectionWhenMultipleChoicesAreLeftWithSingleChoiceTextShouldNotIncorporateExtraTextIntoMainBlock() {
        // GIVEN
        data.setChoices(choicesMixed);
        prepareForSwitch("9");
        setUpNewParagraph();
        setUpModel();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getPosition()).andReturn(1);
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(newParagraph));
        newParagraph.calculateValidEvents();
        expectCpDataInsertion();
        expect(sectionHandlingService.handleSection(model, wrapper, newParagraph, info)).andReturn("done");
        expect(newParagraph.getData()).andReturn(data);
        expect(newParagraph.getData()).andReturn(data);
        expect(sectionHandlingService.resolveParagraphId(info, "10")).andReturn("10");
        expect(sectionHandlingService.resolveParagraphId(info, "9")).andReturn("9");
        expect(wrapper.getParagraph()).andReturn(newParagraph);
        expect(newParagraph.getId()).andReturn("10");
        expect(wrapper.setModel(model)).andReturn(model);
        navigationRecorder.recordNavigation(wrapper, "s-9", oldParagraph, newParagraph);
        expectResources();
        expectCpDataInsertion();
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleSection(model, request, "s-9");
        // THEN
        Assert.assertEquals(data.getText(), DEFAULT_TEXT_WITHOUT_ALT);
        Assert.assertEquals(returned, "done");
    }

    private void expectCpDataInsertion() {
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("rawCharacterPageData", character)).andReturn(charPageData);
        expect(model.addAttribute("charEquipments", charPageData)).andReturn(model);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(wrapper.getPlayer()).andReturn(player);
        wrapper.setPosition(1);
        logger.debug("Handling paragraph {} for book.", id);
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(contentInitializer.loadSection(id, player, oldParagraph, info)).andReturn(newParagraph);
        expect(wrapper.setParagraph(newParagraph)).andReturn(newParagraph);
        expect(wrapper.getCharacter()).andReturn(character);
    }

    private void setUpNewParagraph() {
        expect(newParagraph.getId()).andReturn("10");
        paragraphHandler.addParagraph(character, "10");
        expect(wrapper.getParagraph()).andReturn(newParagraph);
        expect(newParagraph.getId()).andReturn("10");
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

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
