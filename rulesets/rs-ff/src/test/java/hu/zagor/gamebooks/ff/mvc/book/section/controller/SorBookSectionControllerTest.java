package hu.zagor.gamebooks.ff.mvc.book.section.controller;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.section.SorRuleBookParagraphResolver;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.google.common.collect.Sets;

/**
 * Unit test for class {@link SorBookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class SorBookSectionControllerTest {

    @MockControl private IMocksControl mockControl;
    private SorBookSectionController underTest;
    @Mock private Model model;
    @Mock private HttpServletRequest request;
    @Inject private Logger logger;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private SectionHandlingService sectionHandlingService;
    @Mock private Paragraph paragraph;
    @Mock private SorParagraphData data;
    @Mock private List<Choice> spellChoices;
    @Mock private Choice spellChoice;
    @Mock private ChoiceSet choices;
    @Mock private PlayerUser player;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private CharacterParagraphHandler paragraphHandler;
    @Mock private SorCharacter character;
    @Mock private Paragraph newParagraph;
    @Inject private BookContentInitializer contentInitializer;
    @Mock private FfCharacterPageData charPageData;
    @Instance(inject = true) private Map<String, CustomPrePostSectionHandler> prePostHandlers;
    @Mock private PlayerSettings settings;
    @Mock private Map<String, Enemy> enemies;
    @Mock private SorRuleBookParagraphResolver paragraphResolver;
    @Instance private StaticResourceDescriptor staticResourceDescriptor;
    @Instance private List<Choice> detailedSpellChoices;
    @Mock private ContinuationData continueData;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(1);
        info.setContinuationData(continueData);
        info.setCharacterPageDataBeanId("ffCpd");
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setParagraphHandler(paragraphHandler);
        info.setParagraphResolver(paragraphResolver);

        detailedSpellChoices.add(new Choice("290", "SUD", 1, null));
        detailedSpellChoices.add(new Choice("399", "ZUH", 2, null));
        detailedSpellChoices.add(new Choice("479", "GAK", 3, null));
        detailedSpellChoices.add(new Choice("330", "REP", 4, null));
        detailedSpellChoices.add(new Choice("424", "CSA", 5, null));
    }

    @UnderTest
    public SorBookSectionController underTest() {
        return new SorBookSectionController(sectionHandlingService);
    }

    @BeforeMethod
    public void setUpMethod() {
        staticResourceDescriptor.getCss().clear();
        staticResourceDescriptor.getJs().clear();
    }

    public void testHandleSpellSectionChangeBySpellPositionWhenCalledWithSimpleTargetShouldSelectSpellAddItWithUniquePosAndExecuteNormally() {
        // GIVEN
        final Capture<Choice> newSpell = newCapture();

        logger.info("Handling spell section switch to position 'spl-{}'.", "2");
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getSpellChoices()).andReturn(spellChoices);
        expect(spellChoices.get(2)).andReturn(spellChoice);
        expect(spellChoice.getId()).andReturn("479");
        expect(data.getChoices()).andReturn(choices);
        expect(spellChoice.getText()).andReturn("GAK");
        expect(choices.add(capture(newSpell))).andReturn(true);
        logger.info("Handling spell section switch to section '{}'.", "479");

        expectCommon();
        expect(model.addAttribute("cont", continueData)).andReturn(model);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleSpellSectionChangeBySpellPosition(model, request, "2");
        // THEN
        Assert.assertEquals(returned, "ffSection");
        final Choice newChoice = newSpell.getValue();
        Assert.assertEquals(newChoice.getPosition(), 999);
        Assert.assertEquals(newChoice.getText(), "GAK");
        Assert.assertEquals(newChoice.getId(), "479");
        Assert.assertEquals(staticResourceDescriptor.getCss(), Sets.newHashSet("raw.css", "ff.css"));
        Assert.assertEquals(staticResourceDescriptor.getJs(), Sets.newHashSet("raw.js", "ff.js"));
    }

    public void testHandleSpellSectionChangeBySpellPositionWhenCalledWithInformativeTargetShouldSelectSpellAddItWithUniquePosAndExecuteNormally() {
        // GIVEN
        final Capture<Choice> newSpell = newCapture();

        logger.info("Handling spell section switch to position 'spl-{}'.", "479~2");
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getSpellChoices()).andReturn(detailedSpellChoices);

        expect(data.getChoices()).andReturn(choices);
        expect(choices.add(capture(newSpell))).andReturn(true);
        logger.info("Handling spell section switch to section '{}'.", "479");

        expectCommon();
        expect(model.addAttribute("cont", continueData)).andReturn(model);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleSpellSectionChangeBySpellPosition(model, request, "479~2");
        // THEN
        Assert.assertEquals(returned, "ffSection");
        final Choice newChoice = newSpell.getValue();
        Assert.assertEquals(newChoice.getPosition(), 999);
        Assert.assertEquals(newChoice.getText(), "GAK");
        Assert.assertEquals(newChoice.getId(), "479");
        Assert.assertEquals(staticResourceDescriptor.getCss(), Sets.newHashSet("raw.css", "ff.css"));
        Assert.assertEquals(staticResourceDescriptor.getJs(), Sets.newHashSet("raw.js", "ff.js"));
    }

    private void expectCommon() {
        logger.debug("Handling choice request '{}' for book.", "s-479");
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.getChoiceById("479")).andReturn(spellChoice);
        expect(spellChoice.getPosition()).andReturn(999);
        wrapper.setPosition(999);
        expect(spellChoice.getId()).andReturn("479");
        logger.debug("Handling paragraph {} for book.", "479");
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(contentInitializer.loadSection("479", player, paragraph, info)).andReturn(newParagraph);
        expect(wrapper.setParagraph(newParagraph)).andReturn(newParagraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(newParagraph.getId()).andReturn("479");
        paragraphHandler.addParagraph(character, "479");

        // pre
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(newParagraph);
        expect(newParagraph.getId()).andReturn("479");
        expect(character.getCharacterSaveLocations()).andReturn(new HashMap<String, SorCharacter>());
        character.setLastEatenBonus(0);
        expect(wrapper.getParagraph()).andReturn(newParagraph);
        expect(newParagraph.getId()).andReturn("479");

        // do handle section
        expect(wrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);
        expect(newParagraph.getId()).andReturn("479");

        expect(settings.isTopSectionDisplayable()).andReturn(true);
        expect(model.addAttribute("hideTopSection", false)).andReturn(model);
        expect(settings.isChoiceSectionDisplayable()).andReturn(true);
        expect(model.addAttribute("hideChoiceSection", false)).andReturn(model);
        expect(settings.getImageTypeOrder()).andReturn("colBw");
        expect(model.addAttribute("imgTypeOrder", "colBw")).andReturn(model);
        expect(settings.isInformativeSectionsRequested()).andReturn(true);
        expect(model.addAttribute("informativeSections", true)).andReturn(model);

        expect(model.addAttribute("bookInfo", info)).andReturn(model);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getPosition()).andReturn(999);
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(newParagraph));
        newParagraph.calculateValidEvents();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("ffCpd", character, characterHandler)).andReturn(charPageData);
        expect(model.addAttribute("data", charPageData)).andReturn(model);
        expect(sectionHandlingService.handleSection(model, wrapper, newParagraph, info)).andReturn("ffSection");
        expect(newParagraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.size()).andReturn(5);
        expect(data.getText()).andReturn("Some text.");
        data.setText("Some text.");
        expect(newParagraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(new DefaultChoiceSet());
        expect(newParagraph.getData()).andReturn(data);
        expect(data.getSpellChoices()).andReturn(new ArrayList<Choice>());

        // post
        expect(wrapper.getCharacter()).andReturn(character);
        character.setLuckCookieActive(false);
        expect(wrapper.getParagraph()).andReturn(newParagraph);
        expect(newParagraph.getId()).andReturn("479");

        expect(wrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);
        expect(settings.getImageTypeOrder()).andReturn("bwFirst");
        expect(newParagraph.getData()).andReturn(data);
        expect(data.getText()).andReturn("<p>Some text.</p>");
        data.setText("<p>Some text.</p>");

        expect(wrapper.setModel(model)).andReturn(model);

        final Map<String, Object> map = new HashMap<>();
        expect(model.asMap()).andReturn(map);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(staticResourceDescriptor);
        expect(model.asMap()).andReturn(map).times(3);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("ffCpd", character, characterHandler)).andReturn(charPageData);
        expect(model.addAttribute("data", charPageData)).andReturn(model);
    }

}
