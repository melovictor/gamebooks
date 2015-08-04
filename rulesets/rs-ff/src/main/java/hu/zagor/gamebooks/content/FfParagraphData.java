package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;
import hu.zagor.gamebooks.content.command.changeitem.ChangeItemCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended paragraph data for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfParagraphData extends ParagraphData {

    private List<ModifyAttribute> modifyAttributes = new ArrayList<>();
    private CommandList immediateCommands = new CommandList();
    private boolean interrupt;
    private boolean loseBattleRound;
    private boolean canEat;

    public List<ModifyAttribute> getModifyAttributes() {
        return modifyAttributes;
    }

    /**
     * Adds a new {@link ModifyAttribute} object to the paragraph.
     * @param modifyAttribute the new object to add
     */
    public void addModifyAttributes(final ModifyAttribute modifyAttribute) {
        modifyAttributes.add(modifyAttribute);
    }

    public CommandList getImmediateCommands() {
        return immediateCommands;
    }

    /**
     * Adds a new {@link ChangeEnemyCommand} object to the paragraph.
     * @param changeEnemy the new object to add
     */
    public void addEnemyModifyAttributes(final ChangeEnemyCommand changeEnemy) {
        immediateCommands.add(changeEnemy);
    }

    /**
     * Adds a new {@link ChangeItemCommand} object to the paragraph.
     * @param changeItem the new object to add
     */
    public void addItemModifyAttributes(final ChangeItemCommand changeItem) {
        immediateCommands.add(changeItem);
    }

    @Override
    public FfParagraphData clone() throws CloneNotSupportedException {
        final FfParagraphData cloned = (FfParagraphData) super.clone();

        cloned.modifyAttributes = new ArrayList<>();
        for (final ModifyAttribute attrib : modifyAttributes) {
            cloned.modifyAttributes.add(attrib.clone());
        }
        cloned.immediateCommands = new CommandList();
        for (final Command command : this.immediateCommands) {
            cloned.immediateCommands.add(command.clone());
        }

        return cloned;
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    public void setInterrupt(final boolean interrupt) {
        this.interrupt = interrupt;
    }

    public boolean isLoseBattleRound() {
        return loseBattleRound;
    }

    public void setLoseBattleRound(final boolean loseBattleRound) {
        this.loseBattleRound = loseBattleRound;
    }

    public boolean isCanEat() {
        return canEat;
    }

    public void setCanEat(final boolean canEat) {
        this.canEat = canEat;
    }

}
