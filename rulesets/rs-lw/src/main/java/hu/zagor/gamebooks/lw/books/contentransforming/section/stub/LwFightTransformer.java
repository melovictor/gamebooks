package hu.zagor.gamebooks.lw.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.stub.ComplexFightTransformer;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import org.w3c.dom.Node;

/**
 * Fight transformer for the LW rulesystem.
 * @author Tamas_Szekeres
 */
public class LwFightTransformer extends ComplexFightTransformer<LwEnemy, LwFightCommand> {

    @Override
    protected void parseRulesetSpecificAttributes(final LwFightCommand fightCommand, final Node node) {
    }

    @Override
    protected LwFightCommand getFightCommandBean() {
        return getBeanFactory().getBean(LwFightCommand.class);
    }

}
