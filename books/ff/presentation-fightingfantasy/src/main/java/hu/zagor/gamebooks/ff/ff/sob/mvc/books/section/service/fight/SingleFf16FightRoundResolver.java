package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service.fight;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicAbstractCustomEnemyHandlingFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF16.
 * @author Tamas_Szekeres
 */
@Component("singleff16FightRoundResolver")
public class SingleFf16FightRoundResolver extends BasicAbstractCustomEnemyHandlingFightRoundResolver<EnemyPrePostFightDataContainer> {
    private static final int AWKMUTE_STAFF_MESSAGE_POSITION = 3;
    @Autowired @Qualifier("singleFightRoundResolver") private FfFightRoundResolver decorated;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final boolean isAwkmuteStaff = awkmuteStaffUsed(resolvationData);

        int[] randomNumbers = null;
        if (isAwkmuteStaff) {
            randomNumbers = setUpStaffDamage(resolvationData);
        }
        final FightRoundResult[] resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);
        if (isAwkmuteStaff) {
            reportStaffDamage(command, resolvationData, resolveRound, randomNumbers);
        }
        return resolveRound;
    }

    private void reportStaffDamage(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] resolveRound,
        final int[] randomNumbers) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(resolvationData.getCharacter(), LastFightCommand.ENEMY_ID);
        final int enemyPos = getEnemyPos(command.getResolvedEnemies(), enemyId);
        if (resolveRound[enemyPos] == FightRoundResult.WIN) {
            command.getMessages().addKey(AWKMUTE_STAFF_MESSAGE_POSITION, "page.ff.label.random.after", renderer.render(generator.getDefaultDiceSide(), randomNumbers),
                randomNumbers[0]);
            command.getMessages().addKey(AWKMUTE_STAFF_MESSAGE_POSITION + 1, "page.ff16.label.fight.awkmuteStaff" + randomNumbers[0]);
        }
    }

    private int getEnemyPos(final List<FfEnemy> resolvedEnemies, final String enemyId) {
        int pos = 0;
        for (final FfEnemy enemy : resolvedEnemies) {
            if (enemyId.equals(enemy.getId())) {
                break;
            }
            pos++;
        }
        return pos;
    }

    private int[] setUpStaffDamage(final ResolvationData resolvationData) {
        final int[] randomNumber = generator.getRandomNumber(1);
        final FfItem equippedWeapon = getEquippedWeapon(resolvationData);
        if (randomNumber[0] <= 2) {
            equippedWeapon.setStaminaDamage(0);
            equippedWeapon.setSkillDamage(1);
        } else {
            equippedWeapon.setStaminaDamage(2);
            equippedWeapon.setSkillDamage(0);
        }
        return randomNumber;
    }

    private boolean awkmuteStaffUsed(final ResolvationData resolvationData) {
        final FfItem equippedWeapon = getEquippedWeapon(resolvationData);
        return "1003".equals(equippedWeapon.getId());
    }

    private FfItem getEquippedWeapon(final ResolvationData resolvationData) {
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final FfItem equippedWeapon = itemHandler.getEquippedWeapon((FfCharacter) resolvationData.getCharacter());
        return equippedWeapon;
    }

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> getType() {
        return Ff16BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected EnemyPrePostFightDataContainer getDataBean() {
        return new EnemyPrePostFightDataContainer();
    }
}
