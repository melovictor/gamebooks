package hu.zagor.gamebooks.content.command.fight.roundresolver.domain;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.List;

/**
 * Object for moving around fight-relevant beans.
 * @author Tamas_Szekeres
 */
public class FightDataDto {

    private final FfEnemy enemy;
    private final FightCommandMessageList messages;
    private final ResolvationData resolvationData;
    private final List<ItemType> usableWeaponTypes;

    /**
     * Basic constructor.
     * @param enemy the current enemy
     * @param messages the list of messages that will be displayed on the page
     * @param resolvationData the {@link ResolvationData} bean
     * @param usableWeaponTypes the list of {@link ItemType}s which are usable in the current battle
     */
    public FightDataDto(final FfEnemy enemy, final FightCommandMessageList messages, final ResolvationData resolvationData, final List<ItemType> usableWeaponTypes) {
        this.enemy = enemy;
        this.messages = messages;
        this.resolvationData = resolvationData;
        this.usableWeaponTypes = usableWeaponTypes;
    }

    public FfEnemy getEnemy() {
        return enemy;
    }

    public FfCharacter getCharacter() {
        return (FfCharacter) resolvationData.getCharacter();
    }

    public FightCommandMessageList getMessages() {
        return messages;
    }

    public FfAttributeHandler getAttributeHandler() {
        return getCharacterHandler().getAttributeHandler();
    }

    public FfCharacterHandler getCharacterHandler() {
        return (FfCharacterHandler) resolvationData.getCharacterHandler();
    }

    /**
     * Gets the selected weapon using the {@link CharacterItemHandler} from the weapon types specified as usable by the {@link FfFightCommand}.
     * @return the {@link FfItem} for the currently selected weapon
     */
    public FfItem getSelectedWeapon() {
        FfItem equippedWeapon;
        if (usableWeaponTypes == null) {
            equippedWeapon = getItemHandler().getEquippedWeapon(getCharacter());
        } else {
            equippedWeapon = getItemHandler().getEquippedWeapon(getCharacter(), usableWeaponTypes);
        }
        return equippedWeapon;
    }

    private FfCharacterItemHandler getItemHandler() {
        return getCharacterHandler().getItemHandler();
    }

}
