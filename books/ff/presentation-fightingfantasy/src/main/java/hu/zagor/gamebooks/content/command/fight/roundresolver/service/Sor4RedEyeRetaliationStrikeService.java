package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Service for handling the Red Eyes' retaliation eye openings. TODO: must do retaliation when red eye fights red eye!
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
     * Decides whether a retaliation strike is necessary. For it to be necessary, the enemy must be alive and a Red Eye.
     * @param dto the {@link FightDataDto} object
     * @param enemy the current enemy we are facing
     * @return true if retaliation is necessary, false otherwise
     */
    public boolean needToRetaliate(final FightDataDto dto, final FfEnemy enemy) {
        return enemy.getStamina() > 0 && isRedEye(dto);
    }

    private boolean firstHitFromRedEye(final FightDataDto dto) {
        return dto.getCharacterHandler().getInteractionHandler().getLastFightCommand(dto.getCharacter(), "beenHitByRedEye") == null;
    }

    private boolean redEyeHitCharacter(final FightDataDto dto) {
        final int[] randomNumber = generator.getRandomNumber(1);
        final String targetRollText = diceResultRenderer.render(generator.getDefaultDiceSide(), randomNumber);
        final int target = getTargetValue(dto);
        if (target <= randomNumber[0]) {
            dto.getMessages().addKey("page.sor4.fight.redeye.eyeTargeting.hit", targetRollText, randomNumber[0]);
        } else {
            dto.getMessages().addKey("page.sor4.fight.redeye.eyeTargeting.missed", targetRollText, randomNumber[0]);
        }
        return target <= randomNumber[0];
    }

    private int getTargetValue(final FightDataDto dto) {
        final FfUserInteractionHandler interactionHandler = dto.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = dto.getCharacter();
        final String id = dto.getEnemy().getId();
        final String burnAttempt = interactionHandler.getLastFightCommand(character, "redEyeBurnAttempt" + id);
        int targetValue = INITIAL_TARGET_VALUE;
        int lastBurnAttempt;
        if (burnAttempt == null) {
            lastBurnAttempt = 0;
        } else {
            lastBurnAttempt = Integer.parseInt(burnAttempt);
        }
        targetValue -= lastBurnAttempt;
        interactionHandler.setFightCommand(character, "redEyeBurnAttempt" + id, String.valueOf(lastBurnAttempt + 1));
        return targetValue;
    }

    private void causeDamage(final FightDataDto dto) {
        final int damage = generator.getRandomNumber(1)[0];
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.sor4.fight.redeye.hitWithEye", dto.getEnemy().getCommonName(), damage);
        dto.getCharacter().changeStamina(-damage);
        dto.getCharacterHandler().getInteractionHandler().setFightCommand(dto.getCharacter(), "beenHitByRedEye", "");
    }

    private void killCharacter(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfAttributeHandler attributeHandler = dto.getCharacterHandler().getAttributeHandler();
        character.changeStamina(-attributeHandler.resolveValue(character, "stamina"));
        dto.getMessages().addKey("page.sor4.fight.redeye.killWithEye", dto.getEnemy().getCommonName());
    }

    private boolean isRedEye(final FightDataDto dto) {
        return redEyes.contains(dto.getEnemy().getId());
    }
}
