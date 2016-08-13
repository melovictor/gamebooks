package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Fighting Fantasy-related implementation for doing ruleset-specific item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class FfCharacterItemHandler extends ComplexCharacterItemHandler {

    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Override
    public List<Item> removeItem(final Character character, final String itemId, final int amount) {
        final List<Item> removedItems = super.removeItem(character, itemId, amount);
        getEquippedWeapon((FfCharacter) character);
        return removedItems;
    }

    @Override
    public List<Item> removeItem(final Character character, final GatheredLostItem item) {
        final List<Item> removedItems = super.removeItem(character, item);
        getEquippedWeapon((FfCharacter) character);
        return removedItems;
    }

    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        final FfCharacter character = (FfCharacter) characterObject;

        final int result;
        if ("gold".equals(itemId)) {
            character.setGold(character.getGold() + amount);
            result = amount;
        } else {
            result = super.addItem(character, itemId, amount);
        }

        final List<Item> equipment = character.getEquipment();
        final Item item = getItem(equipment, itemId);
        if (item != null) {
            doEquipSanityCheck(equipment, item);
        }

        return result;
    }

    @Override
    protected int addItem(final Character characterObject, final Item itemObject, final int amount) {
        if (itemObject.getItemType() == ItemType.immediate) {
            final FfItem item = (FfItem) itemObject;
            final FfCharacter character = (FfCharacter) characterObject;
            final int stamina = item.getStamina();
            final int skill = item.getSkill();
            final int luck = item.getLuck();
            character.changeLuck(luck);
            character.changeSkill(skill);
            character.changeStamina(stamina);
        }
        return super.addItem(characterObject, itemObject, amount);
    }

    /**
     * Returns the weapon currently selected by the character.
     * @param character the {@link FfCharacter} whose weapon we want to get
     * @return the equipped weapon
     */
    public FfItem getEquippedWeapon(final FfCharacter character) {
        return getEquippedWeapon(character, Arrays.asList(new ItemType[]{ItemType.weapon1, ItemType.weapon2}));
    }

    /**
     * Returns the weapon currently selected by the character.
     * @param character the {@link FfCharacter} whose weapon we want to get
     * @param validWeaponTypes the type of the weapons for which we're looking among the equipped ones
     * @return the equipped weapon
     */
    public FfItem getEquippedWeapon(final FfCharacter character, final List<ItemType> validWeaponTypes) {
        FfItem equippedWeapon = null;
        FfItem firstWeapon = null;
        FfItem defaultWeapon = null;
        for (final Item itemObject : character.getEquipment()) {
            final FfItem item = (FfItem) itemObject;
            if (validWeaponTypes.contains(item.getItemType())) {
                if (isDefaultWeapon(item)) {
                    defaultWeapon = item;
                } else if (isEquipped(item)) {
                    equippedWeapon = item;
                } else if (firstWeapon == null) {
                    firstWeapon = item;
                }
            }
        }
        final FfItem actualEquippedWeapon = equippedWeapon != null ? equippedWeapon : firstWeapon == null ? defaultWeapon : firstWeapon;
        equipUnequipWeapons(defaultWeapon, actualEquippedWeapon);
        return actualEquippedWeapon;
    }

    private void equipUnequipWeapons(final FfItem defaultWeapon, final FfItem actualEquippedWeapon) {
        if (actualEquippedWeapon != null) {
            actualEquippedWeapon.getEquipInfo().setEquipped(true);
        }
        if (actualEquippedWeapon != defaultWeapon && defaultWeapon != null) {
            defaultWeapon.getEquipInfo().setEquipped(false);
        }
    }

    @Override
    public void setItemEquipState(final Character character, final String itemId, final boolean toEquip) {
        super.setItemEquipState(character, itemId, toEquip);
        doEquipSanityCheck(character.getEquipment(), getItem(character, itemId));
        getEquippedWeapon((FfCharacter) character);
    }

    private boolean isDefaultWeapon(final FfItem item) {
        return "defWpn".equals(item.getId());
    }

    private boolean isEquipped(final FfItem item) {
        return item.getEquipInfo().isEquipped();
    }

    /**
     * Consumes an item.
     * @param character the {@link Character} owning the item
     * @param itemId the id of the item to consume
     * @param attributeHandler the {@link FfAttributeHandler} object
     */

    public void consumeItem(final FfCharacter character, final String itemId, final FfAttributeHandler attributeHandler) {
        final FfItem item = (FfItem) getItem(character.getEquipment(), itemId);
        if (item != null && item.getEquipInfo().isConsumable()) {
            if (item.getItemType() == ItemType.provision) {
                applyConsumableChanges(character, item);
                removeItem(character, itemId, 1);
            } else if (item.getItemType() == ItemType.potion) {
                applyConsumableChanges(character, item);
                if (item.getDose() == 1) {
                    removeItem(character, itemId, 1);
                } else {
                    item.setDose(item.getDose() - 1);
                }
            } else {
                throw new IllegalArgumentException("Don't know how to consume item " + item.getId() + " of type " + item.getItemType() + ".");
            }
        }
        attributeHandler.sanityCheck(character);
    }

    private void applyConsumableChanges(final FfCharacter character, final FfItem item) {
        character.changeStamina(item.getStamina());
        character.changeSkill(item.getSkill());
        character.changeLuck(item.getLuck());

        if (item.getStaminaNd6() > 0) {
            character.changeStamina(generator.getRandomNumber(item.getStaminaNd6())[0]);
        }

        character.setInitialStamina(character.getInitialStamina() + item.getInitialStamina());
        character.setInitialSkill(character.getInitialSkill() + item.getInitialSkill());
        character.setInitialLuck(character.getInitialLuck() + item.getInitialLuck());
    }

}
