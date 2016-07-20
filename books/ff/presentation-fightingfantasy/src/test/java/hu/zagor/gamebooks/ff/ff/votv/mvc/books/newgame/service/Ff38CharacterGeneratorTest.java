package hu.zagor.gamebooks.ff.ff.votv.mvc.books.newgame.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.ff.ff.votv.character.Ff38Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38CharacterGenerator}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38CharacterGeneratorTest {

    private IMocksControl mockControl;
    private Ff38CharacterGenerator underTest;
    private RandomNumberGenerator rand;
    private DiceResultRenderer diceRenderer;
    private Ff38Character character;
    private BookContentSpecification bookContentSpecification;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff38CharacterGenerator();
        rand = mockControl.createMock(RandomNumberGenerator.class);
        diceRenderer = mockControl.createMock(DiceResultRenderer.class);
        character = mockControl.createMock(Ff38Character.class);
        bookContentSpecification = new BookContentSpecification();
        bookContentSpecification.setCharacterBackpackSize(10);
        Whitebox.setInternalState(underTest, "rand", rand);
        Whitebox.setInternalState(underTest, "diceRenderer", diceRenderer);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGenerateCharacterShouldGenerateNewCharacter() {
        // GIVEN
        final int[] skill = new int[]{9, 3};
        final int[] stamina = new int[]{20, 6, 2};
        final int[] luck = new int[]{11, 5};
        expect(rand.getRandomNumber(1, 6)).andReturn(skill);
        expect(rand.getRandomNumber(2, 12)).andReturn(stamina);
        expect(rand.getRandomNumber(1, 6)).andReturn(luck);
        character.setBackpackSize(10);
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
        final int[] faith = new int[]{8, 5};
        expect(rand.getRandomNumber(1, 3)).andReturn(faith);
        character.setFaith(8);
        expect(diceRenderer.render(6, faith)).andReturn(" render_8:5");

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
        Assert.assertEquals(returned.get("ffFaith"), "8 render_8:5");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
