package hu.zagor.gamebooks.ff.sor.mvc.books.section.service;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SorBookPreFightHandlingService}.
 * @author Tamas_Szekeres
 */
@Test
public class SorBookPreFightHandlingServiceTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SorBookPreFightHandlingService underTest;
    private FfBookInformations info;
    @Mock private HttpSessionWrapper wrapper;

    @Inject private DiceResultRenderer renderer;
    @Inject private RandomNumberGenerator generator;

    @Inject private MessageSource messageSource;
    @Inject private LocaleProvider localeProvider;
    @Mock private Paragraph paragraph;
    @Mock private SorParagraphData data;
    @Mock private SorCharacter character;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private Map<String, Enemy> enemies;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private FfEnemy enemy;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private List<Item> itemList;
    private final Locale locale = Locale.ENGLISH;
    @Mock private FfItem singleItem;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);
    }

    public void testHandlePreFightItemUsageWhenItemIsUnknownShouldDoNothingAndReturnNull() {
        // GIVEN
        final String itemId = "3001";
        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingDartsAndHitEnemyShouldReportHitAndDiscardThem() {
        // GIVEN
        final String itemId = "3031";
        final String keyPart = "dart";

        final int[] hitResult = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_2_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.success"), aryEq(new Object[]{}), eq(locale))).andReturn("skill success");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_2_", 2, "skill success"}), eq(locale)))
            .andReturn("skill success: 2");
        data.setText("orig<p>skill success: 2</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill success: 2</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Hit"), aryEq(new Object[]{"Orc", ""}), eq(locale))).andReturn("hit Orc");
        data.setText("orig<p>skill success: 2</p><p>hit Orc</p>");

        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingDartsAndMissedEnemyShouldReportMissAndDiscardThem() {
        // GIVEN
        final String itemId = "3031";
        final String keyPart = "dart";

        final int[] hitResult = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_12_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.failure"), aryEq(new Object[]{}), eq(locale))).andReturn("skill failure");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_12_", 12, "skill failure"}), eq(locale)))
            .andReturn("skill failure: 12");
        data.setText("orig<p>skill failure: 12</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill failure: 12</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Missed"), aryEq(new Object[]{"Orc", ""}), eq(locale))).andReturn("missed Orc");
        data.setText("orig<p>skill failure: 12</p><p>missed Orc</p>");

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingRocksAndHitEnemyShouldReportHitAndDiscardThem() {
        // GIVEN
        final String itemId = "3019a";
        final String keyPart = "rock";

        final int[] hitResult = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_2_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.success"), aryEq(new Object[]{}), eq(locale))).andReturn("skill success");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_2_", 2, "skill success"}), eq(locale)))
            .andReturn("skill success: 2");
        data.setText("orig<p>skill success: 2</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill success: 2</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Hit"), aryEq(new Object[]{"Orc", ""}), eq(locale))).andReturn("hit Orc");
        data.setText("orig<p>skill success: 2</p><p>hit Orc</p>");

        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.removeItem(character, "3019", 1)).andReturn(itemList);

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingRocksAndMissedEnemyShouldReportMissAndDiscardThem() {
        // GIVEN
        final String itemId = "3019a";
        final String keyPart = "rock";

        final int[] hitResult = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_12_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.failure"), aryEq(new Object[]{}), eq(locale))).andReturn("skill failure");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_12_", 12, "skill failure"}), eq(locale)))
            .andReturn("skill failure: 12");
        data.setText("orig<p>skill failure: 12</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill failure: 12</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Missed"), aryEq(new Object[]{"Orc", ""}), eq(locale))).andReturn("missed Orc");
        data.setText("orig<p>skill failure: 12</p><p>missed Orc</p>");

        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.removeItem(character, "3019", 1)).andReturn(itemList);

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingChakramAndHitEnemyShouldReportHitAndDiscardThem() {
        // GIVEN
        final String itemId = "3055";
        final String keyPart = "chakram";

        final int[] hitResult = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.getItem(character, itemId)).andReturn(singleItem);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_2_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.success"), aryEq(new Object[]{}), eq(locale))).andReturn("skill success");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_2_", 2, "skill success"}), eq(locale)))
            .andReturn("skill success: 2");
        data.setText("orig<p>skill success: 2</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill success: 2</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Hit"), aryEq(new Object[]{"Orc", ""}), eq(locale))).andReturn("hit Orc");
        data.setText("orig<p>skill success: 2</p><p>hit Orc</p>");

        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNotNull(returned);
        Assert.assertSame(returned, singleItem);
    }

    public void testHandlePreFightItemUsageWhenUsingChakramAndMissedEnemyShouldReportMissAndDiscardThem() {
        // GIVEN
        final String itemId = "3055";
        final String keyPart = "chakram";

        final int[] hitResult = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.getItem(character, itemId)).andReturn(singleItem);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_12_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.failure"), aryEq(new Object[]{}), eq(locale))).andReturn("skill failure");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_12_", 12, "skill failure"}), eq(locale)))
            .andReturn("skill failure: 12");
        data.setText("orig<p>skill failure: 12</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill failure: 12</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Missed"), aryEq(new Object[]{"Orc", ""}), eq(locale))).andReturn("missed Orc");
        data.setText("orig<p>skill failure: 12</p><p>missed Orc</p>");

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNotNull(returned);
        Assert.assertSame(returned, singleItem);
    }

    public void testHandlePreFightItemUsageWhenUsingCrystalBallAndHitEnemyShouldReportHitAndDiscardThem() {
        // GIVEN
        final String itemId = "3060";
        final String keyPart = "crystalBall";

        final int[] hitResult = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.getItem(character, itemId)).andReturn(singleItem);

        final int[] breakRoll = new int[]{2, 2};
        expect(generator.getRandomNumber(1)).andReturn(breakRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, breakRoll)).andReturn("_2_");

        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_2_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.success"), aryEq(new Object[]{}), eq(locale))).andReturn("skill success");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_2_", 2, "skill success"}), eq(locale)))
            .andReturn("skill success: 2");
        data.setText("orig<p>skill success: 2</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill success: 2</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Hit"), aryEq(new Object[]{"Orc", "_2_"}), eq(locale))).andReturn("hit Orc");
        data.setText("orig<p>skill success: 2</p><p>hit Orc</p>");

        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingCrystalBallAndMissedEnemyShouldReportMissAndDiscardThem() {
        // GIVEN
        final String itemId = "3060";
        final String keyPart = "crystalBall";

        final int[] hitResult = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.getItem(character, itemId)).andReturn(singleItem);

        final int[] breakRoll = new int[]{2, 2};
        expect(generator.getRandomNumber(1)).andReturn(breakRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, breakRoll)).andReturn("_2_");

        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_12_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.failure"), aryEq(new Object[]{}), eq(locale))).andReturn("skill failure");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_12_", 12, "skill failure"}), eq(locale)))
            .andReturn("skill failure: 12");
        data.setText("orig<p>skill failure: 12</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill failure: 12</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Missed"), aryEq(new Object[]{"Orc", "_2_"}), eq(locale))).andReturn("missed Orc");
        data.setText("orig<p>skill failure: 12</p><p>missed Orc</p>");

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingCrystalBallWhichBreaksAndHitEnemyShouldReportHitAndDiscardThem() {
        // GIVEN
        final String itemId = "3060";
        final String keyPart = "crystalBall";

        final int[] hitResult = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.getItem(character, itemId)).andReturn(singleItem);

        final int[] breakRoll = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(breakRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, breakRoll)).andReturn("_1_");
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);

        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_2_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.success"), aryEq(new Object[]{}), eq(locale))).andReturn("skill success");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_2_", 2, "skill success"}), eq(locale)))
            .andReturn("skill success: 2");
        data.setText("orig<p>skill success: 2</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill success: 2</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Hit.broken"), aryEq(new Object[]{"Orc", "_1_"}), eq(locale))).andReturn("hit Orc and broke ball");
        data.setText("orig<p>skill success: 2</p><p>hit Orc and broke ball</p>");

        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingCrystalBallWhichBreaksAndMissedEnemyShouldReportMissAndDiscardThem() {
        // GIVEN
        final String itemId = "3060";
        final String keyPart = "crystalBall";

        final int[] hitResult = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(hitResult);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(wrapper.getCharacter()).andReturn(character);
        expectEnemy();
        expect(itemHandler.getItem(character, itemId)).andReturn(singleItem);

        final int[] breakRoll = new int[]{3, 3};
        expect(generator.getRandomNumber(1)).andReturn(breakRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, breakRoll)).andReturn("_3_");
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);

        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, hitResult)).andReturn("_12_");

        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.failure"), aryEq(new Object[]{}), eq(locale))).andReturn("skill failure");

        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.ff.label.test.skill.compact"), aryEq(new Object[]{"_12_", 12, "skill failure"}), eq(locale)))
            .andReturn("skill failure: 12");
        data.setText("orig<p>skill failure: 12</p>");

        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig<p>skill failure: 12</p>");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor." + keyPart + "Missed.broken"), aryEq(new Object[]{"Orc", "_3_"}), eq(locale)))
            .andReturn("missed Orc and broke ball");
        data.setText("orig<p>skill failure: 12</p><p>missed Orc and broke ball</p>");

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingChainButEnemyTooStrongShouldOnlyReportFailure() {
        // GIVEN
        final String itemId = "3044";
        expectEnemy();
        expect(enemy.getStamina()).andReturn(9);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor.magicChain.enemyTooStrong"), aryEq(new Object[]{"Orc"}), eq(locale))).andReturn("Orc too strong.");
        data.setText("orig<p>Orc too strong.</p>");

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenUsingChainOnWeakEnemyShouldReportAndKillEnemy() {
        // GIVEN
        final String itemId = "3044";
        expectEnemy();
        expect(enemy.getStamina()).andReturn(3);
        enemy.setStamina(0);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.removeItem(character, itemId, 1)).andReturn(itemList);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(enemy.getCommonName()).andReturn("Orc");
        expect(data.getText()).andReturn("orig");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor.magicChain.boundEnemy"), aryEq(new Object[]{"Orc"}), eq(locale))).andReturn("Orc bound.");
        data.setText("orig<p>Orc bound.</p>");

        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, itemId);
        // THEN
        Assert.assertNull(returned);
    }

    private void expectEnemy() {
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
    }

}
