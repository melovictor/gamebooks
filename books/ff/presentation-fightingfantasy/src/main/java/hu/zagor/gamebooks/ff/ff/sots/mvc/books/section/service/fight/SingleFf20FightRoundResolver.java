package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicAbstractCustomEnemyHandlingFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.ff.ff.sots.character.Ff20Character;
import hu.zagor.gamebooks.ff.ff.sots.character.SpecialSkill;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF20.
 * @author Tamas_Szekeres
 */
@Component("singleff20FightRoundResolver")
public class SingleFf20FightRoundResolver extends BasicAbstractCustomEnemyHandlingFightRoundResolver<BasicEnemyPrePostFightDataContainer> {
    private static final int NITOKENDZSUTSZU_ATTACK_ROLL_LIMIT = 9;
    private static final int DZSADZSUTSU_INITIAL_DAMAGE = 3;
    @Autowired @Qualifier("singleBaseFf20FightRoundResolver") private FfFightRoundResolver decorated;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final Ff20Character character = (Ff20Character) resolvationData.getCharacter();
        final FfEnemy selectedEnemy = getSelectedEnemy(resolvationData);

        FightRoundResult[] resolveRound;
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
        if (fastDrawRound(command, character, selectedEnemy, equippedWeapon)) {
            resolveRound = executeFastDraw(command, selectedEnemy);
        } else {
            resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);
            if (twoSwordFight(resolvationData, selectedEnemy, command)) {
                resolvationData.getCharacterHandler().getInteractionHandler().setInteractionState(character, "nito", "1");
                super.resolveRound(command, resolvationData, beforeRoundResult);
                resolvationData.getCharacterHandler().getInteractionHandler().getInteractionState(character, "nito");
            }
        }

        return resolveRound;
    }

    private boolean twoSwordFight(final ResolvationData resolvationData, final FfEnemy selectedEnemy, final FfFightCommand command) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final Map<String, Integer> attackStrengths = command.getAttackStrengths();
        final int heroRoll = attackStrengths.get("h_d1_" + selectedEnemy.getId()) + attackStrengths.get("h_d2_" + selectedEnemy.getId());

        return selectedEnemy.getStamina() > 0 && (itemHandler.hasItem(character, "1001") || itemHandler.hasItem(character, "1003"))
            && itemHandler.hasItem(character, "1002") && heroRoll >= NITOKENDZSUTSZU_ATTACK_ROLL_LIMIT;
    }

    private FightRoundResult[] executeFastDraw(final FfFightCommand command, final FfEnemy selectedEnemy) {
        FightRoundResult[] resolveRound;
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        resolveRound = new FightRoundResult[resolvedEnemies.size()];
        int i = 0;
        for (final FfEnemy enemy : resolvedEnemies) {
            if (enemy.getId().equals(selectedEnemy.getId())) {
                resolveRound[i++] = FightRoundResult.WIN;
            } else {
                resolveRound[i++] = FightRoundResult.IDLE;
            }
        }
        selectedEnemy.setStamina(selectedEnemy.getStamina() - DZSADZSUTSU_INITIAL_DAMAGE);
        final FightCommandMessageList messages = command.getMessages();
        messages.setRoundMessage(1);
        messages.addKey("page.ff20.fight.fastDraw", selectedEnemy.getCommonName());
        return resolveRound;
    }

    private boolean fastDrawRound(final FfFightCommand command, final Ff20Character character, final FfEnemy selectedEnemy, final FfItem equippedWeapon) {
        return character.getSpecialSkill() == SpecialSkill.DZSADZSUTSZU && command.getRoundNumber() == 1
            && selectedEnemy.getStamina() == selectedEnemy.getInitialStamina() && !"defWpn".equals(equippedWeapon.getId());
    }

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<BasicEnemyPrePostFightDataContainer>> getType() {
        return Ff20BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected BasicEnemyPrePostFightDataContainer getDataBean() {
        return new BasicEnemyPrePostFightDataContainer();
    }

}
