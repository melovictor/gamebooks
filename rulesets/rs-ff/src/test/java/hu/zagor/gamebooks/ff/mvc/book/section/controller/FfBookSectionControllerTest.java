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
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.domain.ResourceInformation;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.ff.section.FfRuleBookParagraphResolver;
import hu.zagor.gamebooks.mvc.book.controller.domain.StaticResourceDescriptor;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.recording.NavigationRecorder;
import hu.zagor.gamebooks.recording.UserInteractionRecorder;

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
public class FfBookSectionControllerTest {

    private static final String ENEMY_ID = "26";
    private static final Boolean LUCK_ON_HIT = true;
    private static final Boolean LUCK_ON_DEFENSE = false;
    private FfBookSectionController underTest;
    private IMocksControl mockControl;
    private SectionHandlingService sectionHandlingService;
    private Model model;
    private HttpServletRequest request;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private Paragraph oldParagraph;
    private BeanFactory beanFactory;
    private Logger logger;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacter character;
    private CharacterParagraphHandler paragraphHandler;
    private PlayerUser player;
    private FfRuleBookParagraphResolver paragraphResolver;
    private FfCharacterPageData charPageData;
    private BookContentInitializer contentInitializer;
    private FfParagraphData oldData;
    private Map<String, Enemy> enemies;
    private FfUserInteractionHandler interactionHandler;
    private ChoiceSet choiceSet;
    private UserInteractionRecorder interactionRecorder;
    private NavigationRecorder navigationRecorder;
    private Map<String, Object> modelMap;
    private StaticResourceDescriptor staticResourceDescriptor;
    private Set<String> resourceSet;
    private ResourceInformation resources;

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
        info = new FfBookInformations(1L);
        resources = new ResourceInformation();
        resources.setCssResources("ff15,tm2");
        resources.setJsResources("ff15");
        info.setResources(resources);
        characterHandler = new FfCharacterHandler();
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        characterHandler.setInteractionHandler(interactionHandler);
        paragraphHandler = mockControl.createMock(CharacterParagraphHandler.class);
        characterHandler.setParagraphHandler(paragraphHandler);
        info.setCharacterHandler(characterHandler);
        character = new FfCharacter();
        player = new PlayerUser(3, "FireFoX", false);
        paragraphResolver = mockControl.createMock(FfRuleBookParagraphResolver.class);
        info.setParagraphResolver(paragraphResolver);
        charPageData = mockControl.createMock(FfCharacterPageData.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        player.getSettings().setImageTypeOrder("bwFirst");
        oldData = mockControl.createMock(FfParagraphData.class);
        oldParagraph.setData(oldData);
        enemies = new HashMap<String, Enemy>();
        choiceSet = new DefaultChoiceSet(new ChoicePositionComparator());
        choiceSet.add(new Choice("100a", null, 1, null));
        navigationRecorder = mockControl.createMock(NavigationRecorder.class);
        interactionRecorder = mockControl.createMock(UserInteractionRecorder.class);
        resourceSet = mockControl.createMock(Set.class);
        staticResourceDescriptor = mockControl.createMock(StaticResourceDescriptor.class);
        modelMap = mockControl.createMock(Map.class);
        setUpUnderTest();
    }

    private void setUpUnderTest() {
        underTest = new FfBookSectionController(sectionHandlingService);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "info", info);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        Whitebox.setInternalState(underTest, "navigationRecorder", navigationRecorder);
        Whitebox.setInternalState(underTest, "interactionRecorder", interactionRecorder);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testHandleAttributeTestShouldHandleAttributeTestRequest() {
        expectWrapper();
        interactionRecorder.recordAttributeTest(wrapper);
        expectHandleSection();
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleAttributeTest(model, request);
        // THEN
        Assert.assertEquals(returned, "done");
    }

    public void testHandleRandomShouldHandleRandomRequest() {
        expectWrapper();
        interactionRecorder.recordRandomRoll(wrapper);
        expectHandleSection();
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
        interactionRecorder.prepareFightCommand(wrapper, LUCK_ON_HIT, LUCK_ON_DEFENSE, false);
        interactionRecorder.recordFightCommand(wrapper, ENEMY_ID);
        expectHandleSection();
        mockControl.replay();
        // WHEN
        underTest.handleFight(model, request, ENEMY_ID, LUCK_ON_HIT, LUCK_ON_DEFENSE, false);
        // THEN
    }

    private void expectHandleSection() {
        logger.debug("Handling choice request '{}' for book.", (String) null);
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(oldData.getText()).andReturn("<p>blabla text.</p><a id='#iAmAMarker'></a><p>Blabla other text.</p>");
        oldData.setText("<p>blabla text.</p><p>Blabla other text.</p>");
        expect(wrapper.getPlayer()).andReturn(player);
        expect(model.addAttribute("hideTopSection", true)).andReturn(model);
        expect(model.addAttribute("hideChoiceSection", true)).andReturn(model);
        expect(model.addAttribute("imgTypeOrder", "bwFirst")).andReturn(model);
        expect(model.addAttribute("informativeSections", false)).andReturn(model);
        expect(model.addAttribute("bookInfo", info)).andReturn(model);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getPlayer()).andReturn(player);
        paragraphResolver.resolve(anyObject(ResolvationData.class), eq(oldParagraph));
        oldData.calculateValidEvents(oldParagraph);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("ffCharacterPageData", character, characterHandler)).andReturn(charPageData);
        expect(model.addAttribute("charEquipments", charPageData)).andReturn(model);
        expect(sectionHandlingService.handleSection(model, wrapper, oldParagraph, info)).andReturn("done");
        expect(oldData.getChoices()).andReturn(choiceSet);
        expect(oldData.getText()).andReturn("Some text without alternate placeholder.");
        oldData.setText("Some text without alternate placeholder.");
        expect(oldData.getChoices()).andReturn(choiceSet);
        expect(sectionHandlingService.resolveParagraphId(info, "100a")).andReturn("100");
        expect(wrapper.setModel(model)).andReturn(model);
        navigationRecorder.recordNavigation(wrapper, null, oldParagraph, oldParagraph);
        expectResources();
    }

    private void expectResources() {
        expect(model.asMap()).andReturn(modelMap);
        expect(modelMap.get("resources")).andReturn(null);
        expect(beanFactory.getBean(StaticResourceDescriptor.class)).andReturn(staticResourceDescriptor);
        expect(model.addAttribute("resources", staticResourceDescriptor)).andReturn(model);
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
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
