package hu.zagor.gamebooks.ff.mvc.book.section.controller;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestDecision;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.domain.ResourceInformation;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.FfFightCommandForm;
import hu.zagor.gamebooks.ff.section.FfRuleBookParagraphResolver;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookSectionControllerTest {

    private static final String ENEMY_ID = "26";
    private static final Boolean LUCK_ON_HIT = true;
    private static final Boolean LUCK_ON_DEFENSE = false;
    private FfBookSectionController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private SectionHandlingService sectionHandlingService;
    @Mock private Model model;
    @Mock private HttpServletRequest request;
    @Mock private HttpSessionWrapper wrapper;
    private Paragraph oldParagraph;
    @Inject private BeanFactory beanFactory;
    @Inject private Logger logger;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Instance private FfCharacter character;
    @Mock private CharacterParagraphHandler paragraphHandler;
    private PlayerUser player;
    @Mock private FfRuleBookParagraphResolver paragraphResolver;
    @Mock private FfCharacterPageData charPageData;
    @Mock private FfParagraphData oldData;
    @Instance private Map<String, Enemy> enemies;
    @Mock private FfUserInteractionHandler interactionHandler;
    private ChoiceSet choiceSet;
    @Mock private Map<String, Object> modelMap;
    @Mock private StaticResourceDescriptor staticResourceDescriptor;
    @Mock private Set<String> resourceSet;
    @Instance private ResourceInformation resources;
    @Instance(inject = true) private Map<String, CustomPrePostSectionHandler> prePostHandlers;
    @Instance private FfFightCommandForm form;
    @Mock private ContinuationData continueData;

    @BeforeClass
    public void setUpClass() {
        oldParagraph = new Paragraph("9", null, Integer.MAX_VALUE);
        info = new FfBookInformations(1L);
        info.setContinuationData(continueData);
        resources.setCssResources("ff15,tm2");
        resources.setJsResources("ff15");
        info.setResources(resources);
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setParagraphHandler(paragraphHandler);
        info.setCharacterHandler(characterHandler);
        player = new PlayerUser(3, "FireFoX", false);
        info.setParagraphResolver(paragraphResolver);
        player.getSettings().setImageTypeOrder("bwFirst");
        oldParagraph.setData(oldData);
        choiceSet = new DefaultChoiceSet(new ChoicePositionComparator());
        choiceSet.add(new Choice("100a", null, 1, null));
        Whitebox.setInternalState(underTest, "info", info);
    }

    @UnderTest
    public FfBookSectionController underTest() {
        return new FfBookSectionController(sectionHandlingService);
    }

    public void testHandleAttributeTestShouldHandleAttributeTestRequest() {
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        interactionHandler.setAttributeTestResult(character, AttributeTestDecision.TEST);
        expectHandleSection();
        expect(model.addAttribute("cont", continueData)).andReturn(model);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleAttributeTestTesting(model, request);
        // THEN
        Assert.assertEquals(returned, "done");
    }

    public void testHandleRandomShouldHandleRandomRequest() {
        expectWrapper();
        expectHandleSection();
        expect(model.addAttribute("cont", continueData)).andReturn(model);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleRandom(model, request);
        // THEN
        Assert.assertEquals(returned, "done");
    }

    public void testHandleFightShouldSetParametersToInteractionHandlerAndPassControl() {
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character).times(2);
        interactionHandler.setFightCommand(character, "attacking");
        interactionHandler.setFightCommand(character, "enemyId", ENEMY_ID);
        interactionHandler.setFightCommand(character, "luckOnHit", LUCK_ON_HIT.toString());
        interactionHandler.setFightCommand(character, "luckOnDefense", LUCK_ON_DEFENSE.toString());
        interactionHandler.setFightCommand(character, "luckOnOther", "false");
        interactionHandler.setFightCommand(character, "special", null);
        expectHandleSection();
        expect(model.addAttribute("cont", continueData)).andReturn(model);
        expect(beanFactory.getBean("ffCharacterPageData", character, characterHandler)).andReturn(charPageData);
        expect(model.addAttribute("data", charPageData)).andReturn(model);
        mockControl.replay();
        // WHEN
        form.setId(ENEMY_ID);
        form.setHit(LUCK_ON_HIT);
        form.setDef(LUCK_ON_DEFENSE);
        form.setOth(false);
        underTest.handleFight(model, request, form);
        // THEN
    }

    private void expectHandleSection() {
        logger.debug("Handling choice request '{}' for book.", (String) null);
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(oldData.getText()).andReturn("<p>blabla text.</p><a id='#iAmAMarker'></a><p>Blabla other text.</p>");
        oldData.setText("<p>blabla text.</p><p>Blabla other text.</p>");
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(model.addAttribute("hideTopSection", true)).andReturn(model);
        expect(model.addAttribute("hideChoiceSection", true)).andReturn(model);
        expect(model.addAttribute("imgTypeOrder", "bwFirst")).andReturn(model);
        expect(model.addAttribute("informativeSections", false)).andReturn(model);
        expect(model.addAttribute("bookInfo", info)).andReturn(model);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getPosition()).andReturn(9);
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(oldParagraph));
        oldData.calculateValidEvents(oldParagraph);
        expectCpDataInsertion();
        expect(sectionHandlingService.handleSection(model, wrapper, oldParagraph, info)).andReturn("done");
        expect(oldData.getChoices()).andReturn(choiceSet);
        expect(oldData.getText()).andReturn("Some text without alternate placeholder.");
        oldData.setText("Some text without alternate placeholder.");
        expect(oldData.getChoices()).andReturn(choiceSet);
        expect(sectionHandlingService.resolveParagraphId(info, "100a")).andReturn("100");
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(wrapper.setModel(model)).andReturn(model);
        expectResources();
        expectCpDataInsertion();
    }

    private void expectCpDataInsertion() {
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("ffCharacterPageData", character, characterHandler)).andReturn(charPageData);
        expect(model.addAttribute("data", charPageData)).andReturn(model);
    }

    private void expectResources() {
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(staticResourceDescriptor);
        expect(modelMap.put("resources", staticResourceDescriptor)).andReturn(null);
        expect(staticResourceDescriptor.getJs()).andReturn(resourceSet);
        expect(resourceSet.add("ff15.js")).andReturn(true);
        expect(resourceSet.add("raw.js")).andReturn(true);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(staticResourceDescriptor);
        expect(staticResourceDescriptor.getCss()).andReturn(resourceSet);
        expect(resourceSet.add("ff15.css")).andReturn(true);
        expect(resourceSet.add("tm2.css")).andReturn(true);
        expect(resourceSet.add("raw.css")).andReturn(true);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(staticResourceDescriptor);
        expect(staticResourceDescriptor.getJs()).andReturn(resourceSet);
        expect(resourceSet.add("ff15.js")).andReturn(true);
        expect(resourceSet.add("ff.js")).andReturn(true);
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(staticResourceDescriptor);
        expect(staticResourceDescriptor.getCss()).andReturn(resourceSet);
        expect(resourceSet.add("ff15.css")).andReturn(true);
        expect(resourceSet.add("tm2.css")).andReturn(true);
        expect(resourceSet.add("ff.css")).andReturn(true);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
