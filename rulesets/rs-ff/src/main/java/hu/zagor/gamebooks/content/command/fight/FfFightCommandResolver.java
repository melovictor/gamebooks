package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * Bean for resolving a fight command.
 * @author Tamas_Szekeres
 */
public class FfFightCommandResolver extends TypeAwareCommandResolver<FfFightCommand> {

    private Map<String, FightCommandSubResolver> subResolvers;

    @Override
    protected CommandResolveResult doResolveWithResolver(final FfFightCommand command, final ResolvationData resolvationData) {
        command.getMessages().clear();
        final CommandResolveResult result = new CommandResolveResult();
        final List<ParagraphData> resolveList = doResolve(command, resolvationData);
        result.setResolveList(resolveList);
        result.setFinished(!command.isOngoing());
        if (result.isFinished()) {
            resetPreFightItems(resolvationData);
        }
        applyBattleMessages(resolvationData.getParagraph().getData(), command);
        handleBattleDamageRecovery(command, resolvationData);
        return result;
    }

    private void resetPreFightItems(final ResolvationData resolvationData) {
        final Character character = resolvationData.getCharacter();
        final Iterator<Item> itemIterator = resolvationData.getCharacterHandler().getItemHandler().getItemIterator(character);
        while (itemIterator.hasNext()) {
            final FfItem item = (FfItem) itemIterator.next();
            item.setUsedInPreFight(false);
        }
    }

    private void handleBattleDamageRecovery(final FfFightCommand command, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        if (command.isRecoverDamage()) {
            if (command.getRoundNumber() == 0) {
                interactionHandler.setFightCommand(character, "startStamina", String.valueOf(character.getStamina()));
            } else if (!command.isOngoing()) {
                final String startStamina = interactionHandler.getLastFightCommand(character, "startStamina");
                if (startStamina != null) {
                    character.setStamina(Integer.valueOf(startStamina));
                }
            }
        }
    }

    @Override
    protected List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) characterHandler.getInteractionHandler();

        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) characterHandler.getItemHandler();

        if (command.getRoundNumber() == 0 && interactionHandler.peekLastFightCommand(character) == null) {
            handleForceWeaponReplacement(command, character, itemHandler);
        }

        final FightCommandSubResolver fightCommandSubResolver = subResolvers.get(command.getResolver());
        if (fightCommandSubResolver == null) {
            throw new IllegalStateException("Cannot resolve fight command resolver '" + command.getResolver() + "'!");
        }
        final List<ParagraphData> resolvedParagraphs = fightCommandSubResolver.doResolve(command, resolvationData);
        if (!command.isOngoing()) {
            resetOldWeapon(character, itemHandler, command.getReplacementData());
        }
        return resolvedParagraphs;
    }

    private void handleForceWeaponReplacement(final FfFightCommand command, final FfCharacter character, final FfCharacterItemHandler itemHandler) {
        final WeaponReplacementData replacementData = command.getReplacementData();
        if (replacementData != null) {
            final List<String> forceWeapon = replacementData.getForceWeapons();
            if (forceWeapon != null) {
                final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
                replacementData.setOrigWeapon(equippedWeapon.getId());
                if (!forceWeapon.contains(replacementData.getOrigWeapon())) {
                    if (!equippedWeapon.getEquipInfo().isRemovable()) {
                        throw new IllegalStateException(
                            "The hero has the non-removable weapon '" + equippedWeapon.getId() + "' at this point. This should be checked by the story code!");
                    }
                    forceNewWeapon(character, itemHandler, forceWeapon);
                }

                final EquipInfo equipInfo = itemHandler.getEquippedWeapon(character).getEquipInfo();
                replacementData.setSwitchedWeaponRemovable(equipInfo.isRemovable());
                equipInfo.setRemovable(false);
            }
        }
    }

    private void resetOldWeapon(final FfCharacter character, final FfCharacterItemHandler itemHandler, final WeaponReplacementData replacementData) {
        if (replacementData != null) {
            final FfItem replacedWeapon = itemHandler.getEquippedWeapon(character);
            replacedWeapon.getEquipInfo().setRemovable(replacementData.isSwitchedWeaponRemovable());
            itemHandler.setItemEquipState(character, replacementData.getOrigWeapon(), true);
        }
    }

    private void forceNewWeapon(final FfCharacter character, final FfCharacterItemHandler itemHandler, final List<String> forceWeapon) {
        boolean foundWeapon = false;
        for (final String weaponId : forceWeapon) {
            if (!foundWeapon && itemHandler.hasItem(character, weaponId)) {
                itemHandler.setItemEquipState(character, weaponId, true);
                foundWeapon = true;
            }
        }
        if (!foundWeapon) {
            throw new IllegalStateException("Couldn't find any of the forced weapons with the character! We're breaking up now!");
        }
    }

    public void setSubResolvers(final Map<String, FightCommandSubResolver> subResolvers) {
        this.subResolvers = subResolvers;
    }

    /**
     * Converts the message list into a displayable format and appends it to the current message block.
     * @param rootData the root data object
     * @param command the {@link FfFightCommand} object
     */
    protected void applyBattleMessages(final ParagraphData rootData, final FfFightCommand command) {
        final String messages = StringUtils.collectionToDelimitedString(command.getMessages(), "<br />\n");
        if (!messages.isEmpty()) {
            final StringBuilder builder = new StringBuilder(rootData.getText() + "<p>");
            builder.append(messages);
            builder.append("</p>");
            rootData.setText(builder.toString());
        }
    }

}
