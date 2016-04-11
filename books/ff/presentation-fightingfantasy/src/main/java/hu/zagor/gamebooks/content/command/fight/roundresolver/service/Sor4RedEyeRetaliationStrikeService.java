package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Service for handling the Red Eyes' retaliation eye openings.
 * @author Tamas_Szekeres
 */
@Component
public class Sor4RedEyeRetaliationStrikeService {
    private static final int INITIAL_TARGET_VALUE = 6;

    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer diceResultRenderer;
    @Resource(name = "sor4RedEyes") private Set<String> redEyes;

    /**
     * Executes the retaliation strike.
     * @param dto the {@link FightDataDto} object
     */
    public void calculateRetaliationStrike(final FightDataDto dto) {
        if (redEyeHitCharacter(dto)) {
            if (firstHitFromRedEye(dto)) {
                causeDamage(dto);
            } else {
                killCharacter(dto);
            }
        }
    }

    /**
     * Executes a reverse retaliation strike, where an ally Red Eye tries to burn an enemy one.
     * @param dto the {@link FightDataDto} object
     */
    public void calculateInverseRetaliationStrike(final FightDataDto dto) {
        if (redEyeHitRedEye(dto)) {
            causeInverseDamage(dto);
        }
    }

    /**
     * Decides whether a retaliation strike is necessary. For it to be necessary, the enemy must be alive and a Red Eye.
     * @param dto the {@link FightDataDto} object
     * @return true if retaliation is necessary, false otherwise
     */
    public boolean needToRetaliate(final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        final int heroStamina = dto.getCharacterHandler().getAttributeHandler().resolveValue(dto.getCharacter(), "stamina");
        return enemy.getStamina() > 0 && heroStamina > 0 && isRedEye(enemy);
    }

    private boolean firstHitFromRedEye(final FightDataDto dto) {
        return dto.getCharacterHandler().getInteractionHandler().getLastFightCommand(dto.getCharacter(), "beenHitByRedEye") == null;
    }

    private boolean redEyeHitRedEye(final FightDataDto dto) {
        final int[] randomNumber = generator.getRandomNumber(1);
        final String targetRollText = diceResultRenderer.render(generator.getDefaultDiceSide(), randomNumber);
        final int target = getTargetValue(dto);

        final String enemyName = dto.getEnemy().getName();
        if (target <= randomNumber[0]) {
            dto.getMessages().addKey("page.sor4.fight.redeye.eyeTargeting.inverse.hit", targetRollText, randomNumber[0], enemyName);
        } else {
            dto.getMessages().addKey("page.sor4.fight.redeye.eyeTargeting.inverse.missed", targetRollText, randomNumber[0], enemyName);
        }
        return target <= randomNumber[0];
    }

    private boolean redEyeHitCharacter(final FightDataDto dto) {
        final int[] randomNumber = generator.getRandomNumber(1);
        final String targetRollText = diceResultRenderer.render(generator.getDefaultDiceSide(), randomNumber);
        final int target = getTargetValue(dto);

        final FfCharacter character = dto.getCharacter();
        final String allyPostfix = character instanceof FfAllyCharacter ? ".ally" : "";
        final String allyName = character.getName();
        if (target <= randomNumber[0]) {
            dto.getMessages().addKey("page.sor4.fight.redeye.eyeTargeting" + allyPostfix + ".hit", targetRollText, randomNumber[0], allyName);
        } else {
            dto.getMessages().addKey("page.sor4.fight.redeye.eyeTargeting" + allyPostfix + ".missed", targetRollText, randomNumber[0], allyName);
        }
        return target <= randomNumber[0];
    }

    private int getTargetValue(final FightDataDto dto) {
        final FfUserInteractionHandler interactionHandler = dto.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = dto.getCharacter();
        int lastBurnAttempt = 0;
        int targetValue = INITIAL_TARGET_VALUE;
        final String id = dto.getEnemy().getId();
        if (!(character instanceof FfAllyCharacter)) {
            final String burnAttempt = interactionHandler.getLastFightCommand(character, "redEyeBurnAttempt" + id);
            if (burnAttempt != null) {
                lastBurnAttempt = Integer.parseInt(burnAttempt);
            }
        }
        targetValue -= lastBurnAttempt;
        interactionHandler.setFightCommand(character, "redEyeBurnAttempt" + id, String.valueOf(lastBurnAttempt + 1));
        return targetValue;
    }

    private void causeInverseDamage(final FightDataDto dto) {
        final int damage = generator.getRandomNumber(1)[0];
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        messages.addKey("page.sor4.fight.redeye.hitWithEye.inverse", dto.getCharacter().getName(), damage, enemy.getCommonName());
        enemy.setStamina(enemy.getStamina() - damage);
    }

    private void causeDamage(final FightDataDto dto) {
        final int damage = generator.getRandomNumber(1)[0];
        final FightCommandMessageList messages = dto.getMessages();
        final FfCharacter character = dto.getCharacter();
        final String keyPostfix = character instanceof FfAllyCharacter ? ".ally" : "";
        messages.addKey("page.sor4.fight.redeye.hitWithEye" + keyPostfix, dto.getEnemy().getCommonName(), damage, character.getName());
        character.changeStamina(-damage);
        dto.getCharacterHandler().getInteractionHandler().setFightCommand(character, "beenHitByRedEye", "");
    }

    private void killCharacter(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfAttributeHandler attributeHandler = dto.getCharacterHandler().getAttributeHandler();
        character.changeStamina(-attributeHandler.resolveValue(character, "stamina"));
        dto.getMessages().addKey("page.sor4.fight.redeye.killWithEye", dto.getEnemy().getCommonName());
    }

    private boolean isRedEye(final FfEnemy enemy) {
        return redEyes.contains(enemy.getId());
    }

}
