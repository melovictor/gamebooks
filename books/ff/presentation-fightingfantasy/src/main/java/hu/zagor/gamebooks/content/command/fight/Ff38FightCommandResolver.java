package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.SpellResult;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Custom fight command resolver for FF38.
 * @author Tamas_Szekeres
 */
public class Ff38FightCommandResolver extends FightCommandResolver {

    private static final int WEASEL_HIT_LIMIT = 5;
    private static final String HORNED_VAMPIRE_BAT = "34";
    private static final String WHITE_WINE_DEDUCTION = "4011";
    private static final String WHITE_WINE_MARKER = "4010";
    private static final String REGENERATING_RING = "3020";
    private static final String CURSE_OF_SITH_AFFILIATION = "4105";
    private static final String CURSE_OF_SITH_COUNTER = "4004";
    private static final String AUTOMATIC_BLEEDING = "4005";
    private static final int AUTOMATIC_BLEEDING_MAX_AMOUNT = 99;
    private static final String STRONG_HIT = "4013";
    private static final String JANDORS_ARROW = "4014";
    private static final String BASH = "4015";
    private static final int STRONG_HIT_DAMAGE_BONUS = 4;
    private static final int JANDOR_DAMAGE = 6;
    private static final int JANDOR_HALF_DAMAGE = 3;
    private static final String COUNT_HEYDRICH = "40";
    private static final int JANDOR_HEYDRICH_RESISTENCE = 5;
    private static final String KATARINA = "41";
    private static final int KATARINA_FLEE_STAMINA = 5;
    private static final String SLOW_POISON = "4101";
    private List<String> undeadEnemies;
    private List<String> boneEnemies;

    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Override
    public CommandResolveResult resolve(final Command commandObject, final ResolvationData resolvationData) {
        final FightCommand command = (FightCommand) commandObject;
        command.getMessages().clear();
        handleSlowPoison(resolvationData);
        final SpellResult spellResult = handleSpell(command, resolvationData);
        final CommandResolveResult resolve;
        if (spellResult.isRoundSkippable()) {
            resolve = determineSpellResult(command, resolvationData);
        } else {
            prepareNightstar(command, resolvationData, 1);
            resolve = resolve(command, resolvationData);
            prepareNightstar(command, resolvationData, -1);
        }
        if (spellResult.isSpellActive()) {
            final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
            itemHandler.removeItem(resolvationData.getCharacter(), spellResult.getSpellId(), 1);
            if (STRONG_HIT.equals(spellResult.getSpellId())) {
                final FfItem equippedWeapon = itemHandler.getEquippedWeapon((FfCharacter) resolvationData.getCharacter());
                equippedWeapon.setStaminaDamage(equippedWeapon.getStaminaDamage() - STRONG_HIT_DAMAGE_BONUS);
            }
        }
        handleVampireBatWeasel(command, resolvationData, resolve);
        handleWhiteWine(resolve, resolvationData);
        handleSithCurse(resolve, resolvationData);
        handleAutoDamage(resolve, resolvationData);
        handleRegeneratingRing(command, resolve, resolvationData);
        handleKatarina(resolve, command, resolvationData);
        applyBattleMessages(resolvationData.getParagraph().getData(), command);
        return resolve;
    }

    private CommandResolveResult resolve(final FightCommand command, final ResolvationData resolvationData) {
        final CommandResolveResult result = new CommandResolveResult();
        final List<ParagraphData> resolveList = doResolve(command, resolvationData);
        result.setResolveList(resolveList);
        result.setFinished(!command.isOngoing());
        return result;
    }

    private void handleKatarina(final CommandResolveResult resolve, final FightCommand command, final ResolvationData resolvationData) {
        if (KATARINA.equals(command.getEnemies().get(0))) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
            if (characterHandler.getAttributeHandler().resolveValue(character, "stamina") <= KATARINA_FLEE_STAMINA) {
                resolve.add(command.getFlee());
                resolve.setFinished(true);
            }
        }
    }

    private void prepareNightstar(final FightCommand command, final ResolvationData resolvationData, final int change) {
        if (command.getEnemies().contains("40")) {
            final Character character = resolvationData.getCharacter();
            final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
            final FfItem nightstar = (FfItem) itemHandler.getItem(character, "1003");
            if (nightstar != null) {
                nightstar.setAttackStrength(nightstar.getAttackStrength() + change);
            }
        }
    }

    private CommandResolveResult determineSpellResult(final FightCommand command, final ResolvationData resolvationData) {
        final CommandResolveResult result = new CommandResolveResult();
        final List<ParagraphData> resolveList = new ArrayList<>();
        if (allEnemiesDead(command, resolvationData)) {
            final List<FightOutcome> win = command.getWin();
            for (final FightOutcome outcome : win) {
                resolveList.add(outcome.getParagraphData());
            }
            result.setFinished(true);
        }
        result.setResolveList(resolveList);
        return result;
    }

    private boolean allEnemiesDead(final FightCommand command, final ResolvationData resolvationData) {
        boolean allAreDead = true;
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            allAreDead &= enemy.getStamina() <= 0;
        }
        return allAreDead;
    }

    private SpellResult handleSpell(final FightCommand command, final ResolvationData resolvationData) {
        final SpellResult result = new SpellResult();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        if (interactionHandler.peekLastFightCommand(character) != null) {
            final FightCommandMessageList messages = command.getMessages();
            messages.switchToPreFightMessages();
            final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
            final String enemyId = interactionHandler.peekLastFightCommand(character, "enemyId");
            if (itemHandler.hasItem(character, STRONG_HIT)) {
                final FfItem equippedWeapon = itemHandler.getEquippedWeapon((FfCharacter) resolvationData.getCharacter());
                equippedWeapon.setStaminaDamage(equippedWeapon.getStaminaDamage() + STRONG_HIT_DAMAGE_BONUS);
                result.setSpellId(STRONG_HIT);
                messages.addKey("page.ff.fight.spell.strongHit");
            } else if (itemHandler.hasItem(character, JANDORS_ARROW)) {
                handleJandor(resolvationData, result, messages, enemyId);
            } else if (itemHandler.hasItem(character, BASH)) {
                result.setSpellId(BASH);
                result.setRoundSkippable(true);
                if (boneEnemies.contains(enemyId)) {
                    ((FfEnemy) resolvationData.getEnemies().get(enemyId)).setStamina(0);
                    messages.addKey("page.ff.fight.spell.bash.success");
                } else {
                    messages.addKey("page.ff.fight.spell.bash.failure");
                }
            }
            messages.switchToRoundMessages();
        }
        return result;
    }

    private void handleJandor(final ResolvationData resolvationData, final SpellResult result, final FightCommandMessageList messages, final String enemyId) {
        result.setSpellId(JANDORS_ARROW);
        result.setRoundSkippable(true);
        if (undeadEnemies.contains(enemyId)) {
            if (COUNT_HEYDRICH.equals(enemyId)) {
                final int random = generator.getRandomNumber(1)[0];
                final boolean heydrichResisted = random >= JANDOR_HEYDRICH_RESISTENCE;
                messages.addKey("page.ff.fight.spell.jandor.40." + String.valueOf(heydrichResisted));
                if (heydrichResisted) {
                    messages.addKey("page.ff.fight.spell.jandor.success2");
                    damageEnemy((FfEnemy) resolvationData.getEnemies().get(enemyId), JANDOR_HALF_DAMAGE);
                } else {
                    messages.addKey("page.ff.fight.spell.jandor.success");
                    damageEnemy((FfEnemy) resolvationData.getEnemies().get(enemyId), JANDOR_DAMAGE);
                }
            } else {
                damageEnemy((FfEnemy) resolvationData.getEnemies().get(enemyId), JANDOR_DAMAGE);
                messages.addKey("page.ff.fight.spell.jandor.success");
            }
        } else {
            messages.addKey("page.ff.fight.spell.jandor.failure");
        }
    }

    private void damageEnemy(final FfEnemy enemy, final int damage) {
        enemy.setStamina(enemy.getStamina() - damage);
    }

    private void handleVampireBatWeasel(final FightCommand command, final ResolvationData resolvationData, final CommandResolveResult resolve) {
        final String enemyId = command.getEnemies().get(0);
        if (HORNED_VAMPIRE_BAT.equals(enemyId)) {
            final FightRoundBoundingCommand afterBounding = command.getAfterBounding();
            if (afterBounding != null) {
                final RandomCommand random = (RandomCommand) afterBounding.getCommands().get(0);
                final int diceResult = random.getDiceResult();
                if (diceResult >= WEASEL_HIT_LIMIT) {
                    command.setAfterBounding(null);
                }
            }
            final Map<String, Enemy> enemies = resolvationData.getEnemies();
            final FfEnemy vampireBat = (FfEnemy) enemies.get(HORNED_VAMPIRE_BAT);
            if (vampireBat.getStamina() <= 0) {
                resolve.setFinished(true);
                resolve.getResolveList().add(command.getWin().get(0).getParagraphData());
            }
        }
    }

    private void handleWhiteWine(final CommandResolveResult resolve, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        if (itemHandler.hasItem(character, WHITE_WINE_MARKER)) {
            itemHandler.removeItem(character, WHITE_WINE_MARKER, 1);
            itemHandler.addItem(character, WHITE_WINE_DEDUCTION, 1);
        } else if (itemHandler.hasItem(character, WHITE_WINE_DEDUCTION) && resolve.isFinished()) {
            itemHandler.removeItem(character, WHITE_WINE_DEDUCTION, 1);
        }

    }

    private void handleRegeneratingRing(final FightCommand command, final CommandResolveResult resolve, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        if (itemHandler.hasEquippedItem(character, REGENERATING_RING)) {
            if (resolve.isFinished() && characterHandler.getAttributeHandler().isAlive(character)) {
                int healing = 0;
                for (final String enemyId : command.getEnemies()) {
                    final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get(enemyId);
                    if (enemy.getStamina() <= 0) {
                        healing += 2;
                    }
                }
                character.changeStamina(healing);
                command.getMessages().addKey("page.ff.fight.regenerationRing", new Object[]{healing});
            }
        }
    }

    private void handleAutoDamage(final CommandResolveResult resolve, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, AUTOMATIC_BLEEDING)) {
            character.changeStamina(-1);
            itemHandler.removeItem(character, AUTOMATIC_BLEEDING, resolve.isFinished() ? AUTOMATIC_BLEEDING_MAX_AMOUNT : 1);
        }
    }

    private void handleSithCurse(final CommandResolveResult resolve, final ResolvationData resolvationData) {
        if (resolve.isFinished()) {
            final Character character = resolvationData.getCharacter();
            final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
            if (itemHandler.hasItem(character, CURSE_OF_SITH_COUNTER)) {
                itemHandler.removeItem(character, CURSE_OF_SITH_COUNTER, 1);
                if (!itemHandler.hasItem(character, CURSE_OF_SITH_COUNTER)) {
                    itemHandler.removeItem(character, CURSE_OF_SITH_AFFILIATION, 1);
                }
            }
        }
    }

    private void handleSlowPoison(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) characterHandler.getInteractionHandler();
        if (interactionHandler.peekLastFightCommand(character) == null && itemHandler.hasItem(character, SLOW_POISON)) {
            characterHandler.getItemHandler().addItem(character, "4002", 1);
            character.changeStamina(-1);
        }
    }

    public void setUndeadEnemies(final List<String> undeadEnemies) {
        this.undeadEnemies = undeadEnemies;
    }

    public void setBoneEnemies(final List<String> boneEnemies) {
        this.boneEnemies = boneEnemies;
    }

}
