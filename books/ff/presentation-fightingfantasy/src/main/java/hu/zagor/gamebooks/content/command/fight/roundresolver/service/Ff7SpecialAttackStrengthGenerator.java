package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for handling the two special in-fight items, Sog's helmet and the effects of the potion of confusion.
 * @author Tamas_Szekeres
 */
@Component
public class Ff7SpecialAttackStrengthGenerator {

    private static final String SURPRISING_HILL_TROLL = "41";
    private static final String SOGS_HELMET = "3009";
    private static final int[] MAX_ROLL = new int[]{12, 6, 6};
    private static final int[] MIN_ROLL = new int[]{2, 1, 1};
    private static final int SOG_CONFUSION_ROLL_TARGET = 14;
    private static final String SECTION_POTION_OF_CONFUSION = "311";
    private static final String MARKER_SOG_ACTIVE = "900";
    private static final String MARKER_CONFUSION_ACTIVE = "950";

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer renderer;

    /**
     * Calculates the attack strength for the hero based on the in-book items and other effects that might affect the outcome of a battle round.
     * @param character the {@link FfCharacter} object
     * @param command the {@link FightCommand} object
     * @param attributeHandler the {@link FfAttributeHandler} object
     * @return the calculated attack strength for the hero, or null if it should be calculated automatically
     */
    public int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        int[] selfAttackStrength = null;

        if (command.getRoundNumber() == 1) {
            final boolean hasSogHelmet = hasSogHelmet(character.getEquipment());
            final boolean hasPotionOfConfusion = hasPotionOfConfusion(character.getParagraphs());

            final FightCommandMessageList messages = command.getMessages();
            if (hasPotionOfConfusion) {
                final int[] roll = generator.getRandomNumber(1);
                messages.addKey("page.ff.label.random.after", renderer.render(generator.getDefaultDiceSide(), roll), roll[1]);
                if (roll[1] == 1) {
                    messages.addKey("page.ff7.label.potionOfConfusion");
                    addMarker(command, MARKER_CONFUSION_ACTIVE);
                    selfAttackStrength = MIN_ROLL;
                }
            }
            if (hasSogHelmet) {
                messages.addKey("page.ff7.label.sogsHelmet");
                if (hasMarker(command, MARKER_CONFUSION_ACTIVE)) {
                    selfAttackStrength = new int[]{SOG_CONFUSION_ROLL_TARGET, 1, SOG_CONFUSION_ROLL_TARGET - 1 - attributeHandler.resolveValue(character, "skill")};
                } else {
                    selfAttackStrength = MAX_ROLL;
                }
                addMarker(command, MARKER_SOG_ACTIVE);
            } else if (SURPRISING_HILL_TROLL.equals(command.getEnemies().get(0))) {
                selfAttackStrength = MIN_ROLL;
            }
        }

        return selfAttackStrength;
    }

    private void addMarker(final FightCommand command, final String marker) {
        final RoundEvent e = new RoundEvent();
        e.setEnemyId(marker);
        command.getRoundEvents().add(e);
    }

    private boolean hasPotionOfConfusion(final List<String> paragraphs) {
        return paragraphs.contains(SECTION_POTION_OF_CONFUSION);
    }

    /**
     * Calculates the attack strength for the enemies based on the in-book items and other effects that might affect the outcome of a battle round.
     * @param enemy the {@link FfEnemy} object
     * @param command the {@link FightCommand} object
     * @return the calculated attack strength for the enemy, or null if it should be calculated automatically
     */
    public int[] getEnemyAttackStrength(final FfEnemy enemy, final FightCommand command) {
        int[] enemyAttackStrength = null;

        if (command.getRoundNumber() == 1) {
            final boolean sogActive = hasMarker(command, MARKER_SOG_ACTIVE);
            final boolean potionOfConfusionActive = hasMarker(command, MARKER_CONFUSION_ACTIVE);
            if (sogActive && potionOfConfusionActive) {
                final int firstRoll = 7 - enemy.getSkill() / 2;
                enemyAttackStrength = new int[]{SOG_CONFUSION_ROLL_TARGET, firstRoll, SOG_CONFUSION_ROLL_TARGET - firstRoll - enemy.getSkill()};
            } else if (sogActive) {
                enemyAttackStrength = MIN_ROLL;
            } else if (potionOfConfusionActive) {
                enemyAttackStrength = MAX_ROLL;
            } else if (SURPRISING_HILL_TROLL.equals(command.getEnemies().get(0))) {
                enemyAttackStrength = MAX_ROLL;
            }
        }

        return enemyAttackStrength;
    }

    private boolean hasMarker(final FightCommand command, final String marker) {
        boolean matches = false;
        final List<RoundEvent> roundEvents = command.getRoundEvents();
        for (final RoundEvent event : roundEvents) {
            matches |= marker.equals(event.getEnemyId());
        }
        return matches;
    }

    private boolean hasSogHelmet(final List<Item> equipment) {
        boolean hasHelmet = false;

        for (final Item item : equipment) {
            hasHelmet |= SOGS_HELMET.equals(item.getId()) && item.getEquipInfo().isEquipped();
        }

        return hasHelmet;
    }

}
