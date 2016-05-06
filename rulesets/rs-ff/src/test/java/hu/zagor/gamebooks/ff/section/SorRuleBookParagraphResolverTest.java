package hu.zagor.gamebooks.ff.section;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.List;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SorRuleBookParagraphResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SorRuleBookParagraphResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SorRuleBookParagraphResolver underTest;
    @Mock private SorParagraphData subData;
    @Instance private ResolvationData resolvationData;
    @Mock private Paragraph paragraph;
    @Mock private SorParagraphData rootData;
    @Mock private ChoiceSet choiceSet;
    @Mock private List<Choice> spellChoices;
    @Mock private SorCharacter character;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Inject private RandomNumberGenerator generator;
    @Inject private MessageSource messageSource;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(1L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
        resolvationData.setParagraph(paragraph);
        resolvationData.setCharacter(character);
        resolvationData.setInfo(info);
    }

    @BeforeMethod
    public void setUpMethod() {

    }

    public void testExecuteBasicsWhenDoesNotHaveSpellsShouldDoNothing() {
        // GIVEN
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getText()).andReturn("New text.");
        rootData.appendText("New text.");
        expect(subData.getChoices()).andReturn(choiceSet);
        rootData.addChoices(choiceSet);
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getSpellChoices()).andReturn(new ArrayList<Choice>());
        mockControl.replay();
        // WHEN
        underTest.executeBasics(resolvationData, subData);
        // THEN
    }

    public void testExecuteBasicsWhenDoesNotHaveShakingDiseaseShouldDoNothing() {
        // GIVEN
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getText()).andReturn("New text.");
        rootData.appendText("New text.");
        expect(subData.getChoices()).andReturn(choiceSet);
        rootData.addChoices(choiceSet);
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getSpellChoices()).andReturn(spellChoices);
        expect(spellChoices.isEmpty()).andReturn(false);
        expect(itemHandler.hasItem(character, "5008")).andReturn(false);
        rootData.addSpellChoices(spellChoices);
        mockControl.replay();
        // WHEN
        underTest.executeBasics(resolvationData, subData);
        // THEN
    }

    public void testExecuteBasicsWhenHasShakingDiseaseAndThrownSixShouldDoNothing() {
        // GIVEN
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getText()).andReturn("New text.");
        rootData.appendText("New text.");
        expect(subData.getChoices()).andReturn(choiceSet);
        rootData.addChoices(choiceSet);
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getSpellChoices()).andReturn(spellChoices);
        expect(spellChoices.isEmpty()).andReturn(false);
        expect(itemHandler.hasItem(character, "5008")).andReturn(true);
        expect(generator.getRandomNumber(1)).andReturn(new int[]{6, 6});
        expect(spellChoices.size()).andReturn(5);
        rootData.addSpellChoices(spellChoices);
        mockControl.replay();
        // WHEN
        underTest.executeBasics(resolvationData, subData);
        // THEN
    }

    public void testExecuteBasicsWhenHasShakingDiseaseAndThrownLessThanSixShouldFilterOutNonMatchingSpellEntries() {
        // GIVEN
        final Capture<Choice> newSpellChoice = newCapture(CaptureType.ALL);

        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getText()).andReturn("New text.");
        rootData.appendText("New text.");
        expect(subData.getChoices()).andReturn(choiceSet);
        rootData.addChoices(choiceSet);
        expect(paragraph.getData()).andReturn(rootData);
        expect(subData.getSpellChoices()).andReturn(spellChoices);
        expect(spellChoices.isEmpty()).andReturn(false);
        expect(itemHandler.hasItem(character, "5008")).andReturn(true);
        expect(generator.getRandomNumber(1)).andReturn(new int[]{3, 3});
        expect(spellChoices.size()).andReturn(5);

        expect(generator.getRandomNumber(1, 21, 0)).andReturn(new int[]{11, 11});
        expect(messageSource.getMessage("page.sor.spell.encrypted.11", null, null)).andReturn("aaa");
        expect(spellChoices.set(eq(0), capture(newSpellChoice))).andReturn(null);

        expect(generator.getRandomNumber(1, 21, 0)).andReturn(new int[]{5, 5});
        expect(messageSource.getMessage("page.sor.spell.encrypted.5", null, null)).andReturn("bbb");
        expect(spellChoices.set(eq(1), capture(newSpellChoice))).andReturn(null);

        expect(generator.getRandomNumber(1, 21, 0)).andReturn(new int[]{17, 17});
        expect(messageSource.getMessage("page.sor.spell.encrypted.17", null, null)).andReturn("ccc");
        expect(spellChoices.set(eq(3), capture(newSpellChoice))).andReturn(null);

        expect(generator.getRandomNumber(1, 21, 0)).andReturn(new int[]{11, 11});
        expect(messageSource.getMessage("page.sor.spell.encrypted.11", null, null)).andReturn("aaa");
        expect(generator.getRandomNumber(1, 21, 0)).andReturn(new int[]{20, 20});
        expect(messageSource.getMessage("page.sor.spell.encrypted.20", null, null)).andReturn("ddd");
        expect(spellChoices.set(eq(4), capture(newSpellChoice))).andReturn(null);

        rootData.addSpellChoices(spellChoices);
        mockControl.replay();
        // WHEN
        underTest.executeBasics(resolvationData, subData);
        // THEN
        final List<Choice> values = newSpellChoice.getValues();
        verify(values.get(0), "aaa");
        verify(values.get(1), "bbb");
        verify(values.get(2), "ccc");
        verify(values.get(3), "ddd");
    }

    private void verify(final Choice choice, final String text) {
        Assert.assertEquals(choice.getId(), "-1");
        Assert.assertEquals(choice.getPosition(), -1);
        Assert.assertEquals(choice.getText(), text);
        Assert.assertNull(choice.getSingleChoiceText());
    }

}
