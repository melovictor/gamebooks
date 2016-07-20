package hu.zagor.gamebooks.ff.mvc.book.newgame.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultFfCharacterGenerator}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultFfCharacterGeneratorTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private DefaultFfCharacterGenerator underTest;

    @Inject private RandomNumberGenerator rand;
    @Inject private DiceResultRenderer diceRenderer;
    @Mock private FfCharacter character;
    @Instance private BookContentSpecification bookContentSpecification;

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGenerateCharacterWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.generateCharacter(null, bookContentSpecification, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGenerateCharacterWhenBookContentSpecificationIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.generateCharacter(character, null, null);
        // THEN throws exception
    }

    public void testGenerateCharacterWhenCharacterIsAvailableShouldInitializeIt() {
        // GIVEN
        final int[] skill = new int[]{9, 3};
        final int[] stamina = new int[]{20, 6, 2};
        final int[] luck = new int[]{11, 5};
        expect(rand.getRandomNumber(1, 6)).andReturn(skill);
        expect(rand.getRandomNumber(2, 12)).andReturn(stamina);
        expect(rand.getRandomNumber(1, 6)).andReturn(luck);
        character.setBackpackSize(Integer.MAX_VALUE);
        character.setSkill(9);
        character.setInitialSkill(9);
        character.setStamina(20);
        character.setInitialStamina(20);
        character.setLuck(11);
        character.setInitialLuck(11);

        character.setInitialized(true);

        expect(diceRenderer.render(6, skill)).andReturn(" render_9:3");
        expect(diceRenderer.render(6, stamina)).andReturn(" render_20:6/2");
        expect(diceRenderer.render(6, luck)).andReturn(" render_11:5");

        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.generateCharacter(character, bookContentSpecification, null);
        // THEN
        Assert.assertEquals(returned.get("ffSkill"), "9 render_9:3");
        Assert.assertEquals(returned.get("ffStamina"), "20 render_20:6/2");
        Assert.assertEquals(returned.get("ffLuck"), "11 render_11:5");
        Assert.assertEquals(returned.get("ffSkillNumeric"), 9);
        Assert.assertEquals(returned.get("ffStaminaNumeric"), 20);
        Assert.assertEquals(returned.get("ffLuckNumeric"), 11);
    }

    public void testGetRandShouldReturnRand() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final RandomNumberGenerator returned = underTest.getRand();
        // THEN
        Assert.assertSame(returned, rand);
    }

    public void testGetDiceRendererShouldReturnDiceRenderer() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final DiceResultRenderer returned = underTest.getDiceRenderer();
        // THEN
        Assert.assertSame(returned, diceRenderer);
    }

}
