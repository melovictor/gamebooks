package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for Sor2.
 * @author Tamas_Szekeres
 */
@Component("singlesor2FightRoundResolver")
public class SingleSor2FightRoundResolver extends SingleFightRoundResolver {
    private static final int CHAIN_MAKER_CRITICAL_STAMINA_VALUE = 5;
    private static final String WE_SMOKED_WEED = "4017";
    private static final int MISSES_ENEMY_3 = 3;
    private static final int MISSES_ENEMY_5 = 5;
    private static final int DAMAGES_SELF = 1;
    @Resource(name = "sor2WeedSmokerEnemies") private Set<String> weedSmokers;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfCharacterHandler characterHandler = dto.getCharacterHandler();
        if (weSmokedWeed(character, characterHandler)) {
            handleWeedFilledDamageCausing(command, dto, character, characterHandler);
        } else {
            super.damageEnemy(command, dto);
        }
    }

    private void handleWeedFilledDamageCausing(final FightCommand command, final FightDataDto dto, final FfCharacter character,
        final FfCharacterHandler characterHandler) {
        final int[] roll = generator.getRandomNumber(1);
        final String diceImage = renderer.render(generator.getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.random.after", diceImage, roll[0]);
        if (roll[0] == DAMAGES_SELF) {
            final FfItem equippedWeapon = characterHandler.getItemHandler().getEquippedWeapon(character);
            final int staminaDamage = equippedWeapon.getStaminaDamage();
            character.changeStamina(-staminaDamage);
            messages.addKey("page.sor2.weeders.selfHitSelf", staminaDamage);
        } else if (roll[0] == MISSES_ENEMY_3 || roll[0] == MISSES_ENEMY_5) {
            messages.addKey("page.sor2.weeders.selfMissedHit", dto.getEnemy().getName());
        } else {
            super.damageEnemy(command, dto);
        }
    }

    @Override
    protected void damageSelf(final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        if (enemySmokedWeed(enemy)) {
            handleWeedFilledDamageSuffering(dto, enemy);
        } else {
            super.damageSelf(dto);
        }
    }

    private void handleWeedFilledDamageSuffering(final FightDataDto dto, final FfEnemy enemy) {
        final int[] roll = generator.getRandomNumber(1);
        final String diceImage = renderer.render(generator.getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.random.after", diceImage, roll[0]);
        if (roll[0] == DAMAGES_SELF) {
            enemy.setStamina(enemy.getStamina() - 2);
            messages.addKey("page.sor2.weeders.enemyHitSelf", enemy.getName());
        } else if (roll[0] == MISSES_ENEMY_3 || roll[0] == MISSES_ENEMY_5) {
            messages.addKey("page.sor2.weeders.enemyMissedHit", enemy.getName());
        } else {
            super.damageSelf(dto);
        }
    }

    @Override
    void doTieFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        if ("19".equals(enemy.getId())) {
            final FfCharacter character = dto.getCharacter();
            final int currentStamina = dto.getCharacterHandler().getAttributeHandler().resolveValue(character, "stamina");
            if (currentStamina <= CHAIN_MAKER_CRITICAL_STAMINA_VALUE) {
                super.doLoseFight(command, result, enemyIdx, dto);
            } else {
                super.doTieFight(command, result, enemyIdx, dto);
            }
        } else {
            super.doTieFight(command, result, enemyIdx, dto);
        }
    }

    private boolean weSmokedWeed(final FfCharacter character, final FfCharacterHandler characterHandler) {
        return characterHandler.getItemHandler().hasItem(character, WE_SMOKED_WEED);
    }

    private boolean enemySmokedWeed(final FfEnemy ffEnemy) {
        return weedSmokers.contains(ffEnemy.getId());
    }

    public void setWeedSmokers(final Set<String> weedSmokers) {
        this.weedSmokers = weedSmokers;
    }

}
