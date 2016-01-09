package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleFf23FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleFf23FightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SingleFf23FightRoundResolver underTest;
    @Inject private FightRoundResolver superResolver;
    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Mock private FightCommand command;
    @Instance private ResolvationData resolvationData;
    @Mock private FightBeforeRoundResult beforeRoundResult;
    @Mock private FfCharacter character;
    private BookInformations info;
    @Instance private CharacterHandler characterHandler;
    @Mock private FfUserInteractionHandler interactionHandler;
    private FightRoundResult[] resolveResult;
    @Mock private FightCommandMessageList messages;
    @Mock private CharacterItemHandler itemHandler;
    @Mock private FfItem item;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(3L);
        resolvationData.setCharacter(character);
        resolvationData.setInfo(info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        resolveResult = new FightRoundResult[]{FightRoundResult.WIN};
        characterHandler.setItemHandler(itemHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testResolveFleeShouldCallSuper() {
        // GIVEN
        superResolver.resolveFlee(command, resolvationData);
        mockControl.replay();
        // WHEN
        underTest.resolveFlee(command, resolvationData);
        // THEN
    }

    public void testResolveRoundWhenFightingAgainstGenericEnemyShouldCallSuper() {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("1");
        expect(superResolver.resolveRound(command, resolvationData, beforeRoundResult)).andReturn(resolveResult);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    public void testResolveRoundWhenFightingAgainstTentaclesAndNoEnemySwitchIsDoneShouldCallSuper() {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("47");
        expect(interactionHandler.peekLastFightCommand(character, "lastEnemyId")).andReturn("47");
        expect(superResolver.resolveRound(command, resolvationData, beforeRoundResult)).andReturn(resolveResult);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    public void testResolveRoundWhenFightingAgainstTentaclesAndStartWithNewEnemyWithBurningTheHandShouldSetWeaponDamageAndCallSuper() {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("46");
        expect(interactionHandler.peekLastFightCommand(character, "lastEnemyId")).andReturn(null);
        expect(command.getMessages()).andReturn(messages);
        interactionHandler.setFightCommand(character, "lastEnemyId", "46");
        final int[] randomResult = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(randomResult);
        messages.switchToPreRoundMessages();
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomResult)).andReturn("[1]");
        expect(messages.addKey("page.ff.label.random.after", 1, "[1]")).andReturn(true);
        expect(messages.addKey("page.ff23.branch.burnSelf")).andReturn(true);
        character.changeStamina(-1);
        expect(itemHandler.getItem(character, "1001")).andReturn(item);
        item.setStaminaDamage(2);
        expect(superResolver.resolveRound(command, resolvationData, beforeRoundResult)).andReturn(resolveResult);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    public void testResolveRoundWhenFightingAgainstTentaclesAndSwitchEnemyWithExtinguishedLogShouldSetWeaponDamageAndCallSuper() {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("48");
        expect(interactionHandler.peekLastFightCommand(character, "lastEnemyId")).andReturn("47");
        expect(command.getMessages()).andReturn(messages);
        interactionHandler.setFightCommand(character, "lastEnemyId", "48");
        final int[] randomResult = new int[]{4, 4};
        expect(generator.getRandomNumber(1)).andReturn(randomResult);
        messages.switchToPreRoundMessages();
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomResult)).andReturn("[4]");
        expect(messages.addKey("page.ff.label.random.after", 4, "[4]")).andReturn(true);
        expect(messages.addKey("page.ff23.branch.extinguished")).andReturn(true);
        expect(itemHandler.getItem(character, "1001")).andReturn(item);
        item.setStaminaDamage(2);
        expect(superResolver.resolveRound(command, resolvationData, beforeRoundResult)).andReturn(resolveResult);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    @Test(dataProvider = "lastTest")
    public void testResolveRoundWhenFightingAgainstTentaclesAndSwitchEnemyWithGrabbingTheLogShouldSetWeaponDamageAndCallSuper(final String enemyId,
        final String lastEnemyId) {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "lastEnemyId")).andReturn(lastEnemyId);
        expect(command.getMessages()).andReturn(messages);
        interactionHandler.setFightCommand(character, "lastEnemyId", enemyId);
        final int[] randomResult = new int[]{6, 6};
        expect(generator.getRandomNumber(1)).andReturn(randomResult);
        messages.switchToPreRoundMessages();
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomResult)).andReturn("[6]");
        expect(messages.addKey("page.ff.label.random.after", 6, "[6]")).andReturn(true);
        expect(messages.addKey("page.ff23.branch.burns")).andReturn(true);
        expect(itemHandler.getItem(character, "1001")).andReturn(item);
        item.setStaminaDamage(3);
        expect(superResolver.resolveRound(command, resolvationData, beforeRoundResult)).andReturn(resolveResult);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    @DataProvider(name = "lastTest")
    public Object[][] getEnemyIds() {
        return new Object[][]{{"49", "48"}, {"50", "49"}};
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
