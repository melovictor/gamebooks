package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing data to be displayed on the character page.
 * @author Tamas_Szekeres
 */
@Component("ffCharacterPageData")
@Scope("prototype")
public class FfCharacterPageData extends RawCharacterPageData {

    @LogInject private Logger logger;

    private final int skill;
    private final int stamina;
    private final int luck;
    private final int initialSkill;
    private final int initialStamina;
    private final int initialLuck;
    private final List<Item> items = new ArrayList<>();
    private final List<Item> provisions = new ArrayList<>();
    private final List<Item> potions = new ArrayList<>();
    private final List<Item> shadows = new ArrayList<>();
    private final List<Item> preFightItems = new ArrayList<>();
    private final List<Item> atFightItems = new ArrayList<>();
    private final boolean initialized;
    private final int gold;

    private final FfAttributeHandler attributeHandler;
    private final FfCharacter character;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy ruleset.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public FfCharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character);
        this.character = character;
        attributeHandler = handler.getAttributeHandler();

        initialized = character.isInitialized();

        initialSkill = attributeHandler.resolveValue(character, "initialSkill");
        initialStamina = attributeHandler.resolveValue(character, "initialStamina");
        initialLuck = attributeHandler.resolveValue(character, "initialLuck");
        skill = attributeHandler.resolveValue(character, "skill");
        stamina = attributeHandler.resolveValue(character, "stamina");
        luck = attributeHandler.resolveValue(character, "luck");
        gold = character.getGold();

        sortOutEquipments(character);
    }

    private void sortOutEquipments(final FfCharacter character) {
        Item defaultWeapon = null;
        boolean foundAlternateWeapon = false;
        final List<Item> equipments = character.getEquipment();
        for (final Item item : equipments) {
            if (!item.getItemType().isInvisible()) {
                if ("defWpn".equals(item.getId())) {
                    defaultWeapon = item;
                } else {
                    storeItem(item);
                    if (isWeapon(item)) {
                        foundAlternateWeapon = true;
                    }
                    if (isPreFightItem(item)) {
                        preFightItems.add(item);
                    } else if (isAtFightItem(item)) {
                        atFightItems.add(item);
                    }
                }
            } else {
                if (item.getItemType() == ItemType.shadow) {
                    shadows.add(item);
                }
            }
        }
        if (!foundAlternateWeapon && defaultWeapon != null) {
            items.add(defaultWeapon);
        }
    }

    private void storeItem(final Item item) {
        if (item.getItemType() == ItemType.provision) {
            addProvision(item);
        } else if (item.getItemType() == ItemType.potion) {
            potions.add(item);
        } else {
            items.add(item);
        }
    }

    private boolean isPreFightItem(final Item item) {
        return ((FfItem) item).isPreFight();
    }

    private boolean isAtFightItem(final Item item) {
        return ((FfItem) item).isAtFight();
    }

    private void addProvision(final Item item) {
        final Item foundItem = getExistingProvision(item.getId());
        if (foundItem == null) {
            provisions.add(item.clone());
        } else {
            foundItem.setAmount(foundItem.getAmount() + 1);
        }
    }

    private Item getExistingProvision(final String id) {
        Item found = null;
        for (final Item item : provisions) {
            if (id.equals(item.getId())) {
                found = item;
            }
        }
        return found;
    }

    private boolean isWeapon(final Item item) {
        return item.getItemType() == ItemType.weapon1 || item.getItemType() == ItemType.weapon2;
    }

    public int getSkill() {
        return skill;
    }

    public int getStamina() {
        return stamina;
    }

    public int getLuck() {
        return luck;
    }

    public int getInitialSkill() {
        return initialSkill;
    }

    public int getInitialStamina() {
        return initialStamina;
    }

    public int getInitialLuck() {
        return initialLuck;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isAlive() {
        return !initialized || attributeHandler.isAlive(character);
    }

    public int getGold() {
        return gold;
    }

    public List<Item> getProvisions() {
        return provisions;
    }

    public List<Item> getPotions() {
        return potions;
    }

    public List<Item> getShadows() {
        return shadows;
    }

    public List<Item> getPreFightItems() {
        return preFightItems;
    }

    public List<Item> getAtFightItems() {
        return atFightItems;
    }

}
