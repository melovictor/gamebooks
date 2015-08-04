package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Command for checking items availability with the current character.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class ItemCheckCommand extends Command {

    private String id;
    private CheckType checkType;
    private int amount;

    private ParagraphData have;
    private ParagraphData haveEquipped;
    private ParagraphData dontHave;
    private ParagraphData after;

    @Override
    public ItemCheckCommand clone() throws CloneNotSupportedException {
        final ItemCheckCommand cloned = (ItemCheckCommand) super.clone();
        cloned.have = cloneObject(have);
        cloned.haveEquipped = cloneObject(haveEquipped);
        cloned.dontHave = cloneObject(dontHave);
        cloned.after = cloneObject(after);
        return cloned;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(final CheckType checkType) {
        this.checkType = checkType;
    }

    public ParagraphData getHave() {
        return have;
    }

    public void setHave(final ParagraphData have) {
        this.have = have;
    }

    public ParagraphData getDontHave() {
        return dontHave;
    }

    public void setDontHave(final ParagraphData dontHave) {
        this.dontHave = dontHave;
    }

    public ParagraphData getAfter() {
        return after;
    }

    public void setAfter(final ParagraphData after) {
        this.after = after;
    }

    public ParagraphData getHaveEquipped() {
        return haveEquipped;
    }

    public void setHaveEquipped(final ParagraphData haveEquipped) {
        this.haveEquipped = haveEquipped;
    }

    @Override
    public String toString() {
        return "ItemCheckCommand: " + checkType.toString() + " " + id;
    }

    @Override
    public String getValidMove() {
        return null;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

}
