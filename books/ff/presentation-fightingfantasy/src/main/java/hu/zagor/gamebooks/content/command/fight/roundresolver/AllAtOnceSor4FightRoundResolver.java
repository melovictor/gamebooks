package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * All at once fight round resolver for Sor4.
 * @author Tamas_Szekeres
 */
@Component("allAtOncesor4FightRoundResolver")
public class AllAtOnceSor4FightRoundResolver extends AllAtOnceSorFightRoundResolver {
    private static final int INITIAL_TARGET_VALUE = 6;
    @Resource(name = "sor4Sightmasters") private Set<String> sightmasters;
    @Resource(name = "sor4RedEyes") private Set<String> redEyes;

    @Override
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        super.damageEnemy(command, dto);
        final FfEnemy enemy = dto.getEnemy();
        if (enemy.getStamina() <= 0 && isSightmaster(enemy) && underSpell(dto)) {
            command.getRoundEvents().get(0).getParagraphData().setInterrupt(true);
        }
        if (enemy.getStamina() > 0 && isRedEye(dto)) {
            calculateRetaliationStrike(dto);
        }
    }

    private void calculateRetaliationStrike(final FightDataDto dto) {
        if (redEyeHitCharacter(dto)) {
            if (firstHitFromRedEye(dto)) {
                causeDamage(dto);
            } else {
                killCharacter(dto);
            }
        }
    }

    private boolean firstHitFromRedEye(final FightDataDto dto) {
        return dto.getCharacterHandler().getInteractionHandler().getLastFightCommand(dto.getCharacter(), "beenHitByRedEye") == null;
    }

    private boolean redEyeHitCharacter(final FightDataDto dto) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        final String targetRollText = getDiceResultRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber);
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
        final int damage = getGenerator().getRandomNumber(1)[0];
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

    private boolean underSpell(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfCharacterItemHandler itemHandler = dto.getCharacterHandler().getItemHandler();
        return itemHandler.hasItem(character, "4084");
    }

    private boolean isSightmaster(final FfEnemy enemy) {
        return sightmasters.contains(enemy.getId());
    }
}
