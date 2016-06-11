package hu.zagor.gamebooks.ff.ff.sots.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import java.util.Iterator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data for FF20.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff20CharacterPageData extends FfCharacterPageData {
    private static final int WILLOW_ARROW = 1;
    private static final int BOWEL_ARROW = 2;
    private static final int AP_ARROW = 3;
    private static final int HUMMING_ARROW = 4;
    private static final int TSUNEVARA_ARROW = 5;
    private final int honor;
    private final SpecialSkill specialSkill;
    private ArrowInventory arrows;

    /**
     * Basic constructor.
     * @param character the {@link Ff20Character} object
     * @param handler the {@link FfCharacterHandler} object
     */
    public Ff20CharacterPageData(final Ff20Character character, final FfCharacterHandler handler) {
        super(character, handler);
        honor = character.getHonor();
        specialSkill = character.getSpecialSkill();
        fillArrows();
    }

    private void fillArrows() {
        if (specialSkill == SpecialSkill.KJUDZSUTSZU) {
            arrows = new ArrowInventory();
            final Iterator<Item> items = getItems().iterator();
            while (items.hasNext()) {
                final Item next = items.next();
                if (isArrow(next)) {
                    addArrow(next);
                    items.remove();
                }
            }
        }
    }

    private void addArrow(final Item next) {
        final int slot = Integer.parseInt(next.getId().substring(3));
        switch (slot) {
        case WILLOW_ARROW:
            arrows.setWillowLeaf(arrows.getWillowLeaf() + 1);
            break;
        case BOWEL_ARROW:
            arrows.setBowelRaker(arrows.getBowelRaker() + 1);
            break;
        case AP_ARROW:
            arrows.setArmourPiercer(arrows.getArmourPiercer() + 1);
            break;
        case HUMMING_ARROW:
            arrows.setHummingBulb(arrows.getHummingBulb() + 1);
            break;
        case TSUNEVARA_ARROW:
            arrows.setTsunevara(arrows.getTsunevara() + 1);
            break;
        default:
            throw new IllegalStateException("Detected 'arrow' item with ID " + slot);
        }
    }

    private boolean isArrow(final Item next) {
        return next.getId().startsWith("310");
    }

    public int getHonor() {
        return honor;
    }

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

    public ArrowInventory getArrows() {
        return arrows;
    }

}
