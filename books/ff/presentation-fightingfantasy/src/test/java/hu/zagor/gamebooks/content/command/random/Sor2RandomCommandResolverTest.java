package hu.zagor.gamebooks.content.command.random;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Sor2RandomCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test

public class Sor2RandomCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Sor2RandomCommandResolver underTest;
    @Inject private LocaleProvider localeProvider;
    @Inject private HierarchicalMessageSource messageSource;
    @Inject private Logger logger;
    @Inject private RandomNumberGenerator generator;
    @Inject private BeanFactory beanFactory;
    @Inject private ExpressionResolver expressionResolver;
    private ResolvationData resolvationData;
    @Instance private RandomCommand command;
    private Paragraph paragraph;
    private BookInformations info;
    @Instance private SorCharacter character;
    @Instance private ParagraphData data;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfUserInteractionHandler interactionHandler;
    private final Locale locale = Locale.ENGLISH;
    @Mock private DiceConfiguration diceConfig;
    @Inject private DiceResultRenderer diceRenderer;
    @Instance private RandomResult randomResult;
    @Instance private ParagraphData resultData;
    @Mock private FfCharacterItemHandler itemHandler;
    private Item item3001;
    private Item item3002;
    private Item item3004;
    private Item item3005;
    @Mock private Item gnomeItem;
    @Instance(type = DefaultChoiceSet.class) private ChoiceSet choices;
    private boolean sectionFlag;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        command.setLabel("label");
        command.setDiceConfig("1d6");
        command.getResults().add(randomResult);
        randomResult.setMin("1");
        randomResult.setMax("6");
        randomResult.setParagraphData(resultData);
        characterHandler.setItemHandler(itemHandler);
        item3001 = new Item("3001", "Flute", ItemType.common);
        item3002 = new Item("3002", "Sand", ItemType.common);
        item3004 = new Item("3004", "Diamond", ItemType.valuable);
        item3005 = new Item("3005", "Luck talisman", ItemType.necklace);
        data.setChoices(choices);
    }

    @BeforeMethod
    public void setUpMethod() {
        paragraph = new Paragraph(sectionFlag ? "264" : "264c", "264", 99);
        sectionFlag = !sectionFlag;
        paragraph.setData(data);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        character.getEquipment().clear();
        choices.clear();
        mockControl.reset();
    }

    public void testDoResolveWhenRolled6FirstTimeOnGnomeSectionShouldRemoveTwoItems() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter but takes two items (XXX and YYY) in exchange for the honeycomb.[/p]");
        final int[] rolledValue = new int[]{6, 6};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("3001,3002,3005,3004");
        final Capture<DiceConfiguration> diceConfigCapture = Capture.<DiceConfiguration>newInstance(CaptureType.ALL);
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{6, 3, 3});
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{7, 2, 5});
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(Arrays.asList(item3001));
        expect(itemHandler.removeItem(character, "3004", 1)).andReturn(Arrays.asList(item3004));
        interactionHandler.setInteractionState(character, "gnomeHagglingItems", "++++,3002,3005,++++");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        final List<DiceConfiguration> dualItemConfigs = diceConfigCapture.getValues();
        final DiceConfiguration dualItemConfig = dualItemConfigs.get(0);
        Assert.assertSame(dualItemConfig, dualItemConfigs.get(1));
        Assert.assertEquals(dualItemConfig.getMaxValue(), 5);
        Assert.assertEquals(dualItemConfig.getMinValue(), 2);
        Assert.assertEquals(dualItemConfig.getDiceNumber(), 2);
        Assert.assertTrue(resultData.getText().contains("(Flute and Diamond)"));
    }

    public void testDoResolveWhenRolled6SecondTimeOnGnomeSectionShouldReportGnomeUninterestedAndNotRemoveItems() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter but takes two items (XXX and YYY) in exchange for the honeycomb.[/p]");
        final int[] rolledValue = new int[]{6, 6};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("++++,3002,3005,++++");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage("page.sor2.gnomeHaggling.notInterested", null, locale)).andReturn("[p]Gnome not interested.[/p]");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("Gnome not interested"));
    }

    public void testDoResolveWhenRolled4FirstTimeOnGnomeSectionShouldReportSuccessfulTrade() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter. It hands you over an item (XXX), and takes one of yours (YYY).[/p]");
        final int[] rolledValue = new int[]{4, 4};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("3001,3002,3005,3004");
        expect(itemHandler.removeItem(character, "3005", 1)).andReturn(Arrays.asList(item3005));
        expect(itemHandler.addItem(character, "3050", 1)).andReturn(1);
        expect(itemHandler.getItem(character, "3050")).andReturn(gnomeItem);
        interactionHandler.setInteractionState(character, "gnomeHagglingItems", "3001,3002,----,3004");
        expect(gnomeItem.getName()).andReturn("Big Backpack");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("item (Big Backpack), and"));
        Assert.assertTrue(resultData.getText().contains("yours (Luck talisman).</p"));
    }

    public void testDoResolveWhenRolled2FirstTimeOnGnomeSectionShouldReportSuccessfulTrade() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter. It hands you over an item (XXX), and takes one of yours (YYY).[/p]");
        final int[] rolledValue = new int[]{2, 2};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("3001,3002,3005,3004");
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(Arrays.asList(item3001));
        expect(itemHandler.addItem(character, "3052", 1)).andReturn(1);
        expect(itemHandler.getItem(character, "3052")).andReturn(gnomeItem);
        interactionHandler.setInteractionState(character, "gnomeHagglingItems", "----,3002,3005,3004");
        expect(gnomeItem.getName()).andReturn("Big Backpack");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("item (Big Backpack), and"));
        Assert.assertTrue(resultData.getText().contains("yours (Flute).</p"));
    }

    public void testDoResolveWhenRolled5SecondTimeAfter4OnGnomeSectionShouldReportSuccessfulTrade() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter. It hands you over an item (XXX), and takes one of yours (YYY).[/p]");
        final int[] rolledValue = new int[]{5, 5};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("3001,3002,----,3004");
        expect(itemHandler.removeItem(character, "3004", 1)).andReturn(Arrays.asList(item3004));
        expect(itemHandler.addItem(character, "4040", 1)).andReturn(1);
        expect(itemHandler.getItem(character, "4040")).andReturn(gnomeItem);
        interactionHandler.setInteractionState(character, "gnomeHagglingItems", "3001,3002,----,----");
        expect(gnomeItem.getName()).andReturn("Big Backpack");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("item (Big Backpack), and"));
        Assert.assertTrue(resultData.getText().contains("yours (Diamond).</p"));
    }

    public void testDoResolveWhenRolled4SecondTimeAfter4OnGnomeSectionShouldReportUninterestedAndNotRemoveItem() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter. It hands you over an item (XXX), and takes one of yours (YYY).[/p]");
        final int[] rolledValue = new int[]{4, 4};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("3001,3002,----,3004");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage("page.sor2.gnomeHaggling.notInterested", null, locale)).andReturn("Gnome not interested");

        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("Gnome not interested"));
    }

    public void testDoResolveWhenRolled3SecondTimeAfter6OnGnomeSectionShouldReportFreeGiveaway() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter. It hands you over an item (XXX), and takes one of yours (YYY).[/p]");
        final int[] rolledValue = new int[]{3, 3};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("3001,++++,++++,3004");
        expect(itemHandler.addItem(character, "3051", 1)).andReturn(1);
        expect(itemHandler.getItem(character, "3051")).andReturn(gnomeItem);
        expect(gnomeItem.getName()).andReturn("Big Backpack");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage(eq("page.sor2.gnomeHaggling.freeGiveUp"), aryEq(new Object[]{"Big Backpack"}), eq(locale)))
            .andReturn("Gnome gives away the item (Big Backpack) for free.");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("Gnome gives away the item (Big Backpack) for free."));
    }

    public void testDoResolveWhenRolled6SecondTimeAfter4SOnGnomeSectionShouldGetNonHittingPairAndTrade() {
        // GIVEN
        resultData.setText("[p]The Gnome agrees to the barter but takes two items (XXX and YYY) in exchange for the honeycomb.[/p]");
        final int[] rolledValue = new int[]{6, 6};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingItems")).andReturn("----,3002,3005,3004");
        final Capture<DiceConfiguration> diceConfigCapture = Capture.<DiceConfiguration>newInstance(CaptureType.ALL);
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{6, 2, 4});
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{7, 5, 2});
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{7, 5, 3});
        expect(itemHandler.removeItem(character, "3004", 1)).andReturn(Arrays.asList(item3004));
        expect(itemHandler.removeItem(character, "3002", 1)).andReturn(Arrays.asList(item3002));
        interactionHandler.setInteractionState(character, "gnomeHagglingItems", "----,++++,3005,++++");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        final List<DiceConfiguration> dualItemConfigs = diceConfigCapture.getValues();
        final DiceConfiguration dualItemConfig = dualItemConfigs.get(0);
        Assert.assertSame(dualItemConfig, dualItemConfigs.get(1));
        Assert.assertEquals(dualItemConfig.getMaxValue(), 5);
        Assert.assertEquals(dualItemConfig.getMinValue(), 2);
        Assert.assertEquals(dualItemConfig.getDiceNumber(), 2);
        Assert.assertTrue(resultData.getText().contains("(Diamond and Sand)"));
    }

    public void testDoResolveWhenRolled1SecondTimeAfter1OnGnomeSectionShouldReportUninterestedAndNotRemoveAnything() {
        // GIVEN
        resultData.setText("[p]The Gnome doesn't like any of the items you chose and you must give him something else for the Flute.[/p]");
        final int[] rolledValue = new int[]{1, 1};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingOriginalItems")).andReturn("3001,3002,3005,3004,////");
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(messageSource.getMessage("page.sor2.gnomeHaggling.notInterested", null, locale)).andReturn("Gnome not interested");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("Gnome not interested"));
        final Choice choice = choices.getChoiceByPosition(0);
        Assert.assertEquals(choice.getId(), "264d");
    }

    public void testDoResolveWhenRolled1FirstTimeOnGnomeSectionShouldOfferNonSelectedApplicableItemsForExchange() {
        // GIVEN
        resultData.setText("[p]The Gnome doesn't like any of the items you chose and you must give him something else for the Flute.[/p]");
        final int[] rolledValue = new int[]{1, 1};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingOriginalItems")).andReturn("3001,3002,3005,3004");
        expect(localeProvider.getLocale()).andReturn(locale);
        character.getEquipment()
            .addAll(Arrays.asList(item3001, item3002, item3004, item3005, new Item("2000", "Provision", ItemType.provision), new Item("1001", "Sword", ItemType.weapon1),
                new Item("1002", "Battle axe", ItemType.weapon2), new Item("3003", "Goblin teeth", ItemType.common), new Item("3010", "Goat cheese", ItemType.common),
                new Item("5001", "Plague", ItemType.curseSickness), new Item("4102", "we are wizards", ItemType.shadow)));
        expect(messageSource.getMessage(eq("page.sor2.gnomeHaggling.itemChoice"), aryEq(new Object[]{"Goblin teeth"}), eq(locale))).andReturn("Goblin teeth?");
        expect(messageSource.getMessage(eq("page.sor2.gnomeHaggling.itemChoice"), aryEq(new Object[]{"Goat cheese"}), eq(locale))).andReturn("Goat cheese?");
        interactionHandler.setInteractionState(character, "gnomeHagglingOriginalItems", "3001,3002,3005,3004,////");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("The Gnome doesn't like any of the items you chose and you must give him something else for the Flute"));
        Assert.assertNull(choices.getChoiceByPosition(2000));
        Assert.assertNull(choices.getChoiceByPosition(3001));
        Assert.assertNull(choices.getChoiceByPosition(3002));
        Assert.assertNull(choices.getChoiceByPosition(3004));
        Assert.assertNull(choices.getChoiceByPosition(3005));
        Assert.assertNull(choices.getChoiceByPosition(1001));
        Assert.assertNull(choices.getChoiceByPosition(1002));
        Assert.assertNull(choices.getChoiceByPosition(5001));
        Assert.assertNull(choices.getChoiceByPosition(4102));
        Choice choice = choices.getChoiceByPosition(3003);
        Assert.assertEquals(choice.getId(), "264b");
        choice = choices.getChoiceByPosition(3010);
        Assert.assertEquals(choice.getId(), "264b");
        Assert.assertEquals(choices.size(), 2);
    }

    public void testDoResolveWhenRolled1OnGnomeSectionAndHasNoOtherItemShouldReportUninterested() {
        // GIVEN
        resultData.setText("[p]The Gnome doesn't like any of the items you chose and you must give him something else for the Flute.[/p]");
        final int[] rolledValue = new int[]{1, 1};
        prepareRandom(rolledValue);
        expect(interactionHandler.getInteractionState(character, "gnomeHagglingOriginalItems")).andReturn("3001,3002,3005,3004");
        expect(localeProvider.getLocale()).andReturn(locale);
        character.getEquipment()
            .addAll(Arrays.asList(item3001, item3002, item3004, item3005, new Item("2000", "Provision", ItemType.provision), new Item("1001", "Sword", ItemType.weapon1),
                new Item("1002", "Battle axe", ItemType.weapon2), new Item("5001", "Plague", ItemType.curseSickness),
                new Item("4102", "we are wizards", ItemType.shadow)));
        interactionHandler.setInteractionState(character, "gnomeHagglingOriginalItems", "3001,3002,3005,3004,////");
        expect(messageSource.getMessage("page.sor2.gnomeHaggling.notInterested", null, locale)).andReturn("[p]Gnome not interested.[/p]");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resultData.getText().contains("Gnome not interested"));
        Assert.assertEquals(choices.size(), 1);
        final Choice choice = choices.getChoiceByPosition(0);
        Assert.assertEquals(choice.getId(), "264d");
    }

    public void testDoResolveWhenSectionIsNotOneOfGnomeShouldDoUsualRandomGeneration() {
        // GIVEN
        paragraph = new Paragraph("11", "11", 99);
        paragraph.setData(data);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();

        final int[] rolledValue = new int[]{3, 3};
        prepareRandom(rolledValue);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        final ParagraphData paragraphData = returned.get(0);
        Assert.assertSame(paragraphData, resultData);
    }

    public void testDoResolveWhenGnomeSectionButFirstRoundWithoutRollingYetShouldOnlyInitializeRandom() {
        // GIVEN
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(interactionHandler.hasRandomResult(character)).andReturn(false);

        interactionHandler.setRandomResult(character);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
    }

    private void prepareRandom(final int[] rolledValue) {
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(interactionHandler.hasRandomResult(character)).andReturn(true);
        expect(beanFactory.getBean("1d6", DiceConfiguration.class)).andReturn(diceConfig);
        expect(generator.getRandomNumber(diceConfig)).andReturn(rolledValue);
        logger.debug("Random command generated the number '{}'.", rolledValue[0]);
        expect(diceRenderer.render(diceConfig, rolledValue)).andReturn("result: " + rolledValue[0]);
        expect(messageSource.getMessage(eq("page.raw.label.random.after"), aryEq(new Object[]{"result: " + rolledValue[0], rolledValue[0]}), eq(locale)))
            .andReturn("Result: " + rolledValue[0] + ".");
        expect(expressionResolver.resolveValue(character, "1")).andReturn(1);
        expect(expressionResolver.resolveValue(character, "6")).andReturn(6);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
