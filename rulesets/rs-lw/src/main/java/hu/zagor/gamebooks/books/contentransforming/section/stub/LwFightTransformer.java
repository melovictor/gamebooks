package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.character.enemy.LwEnemy;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;
import org.w3c.dom.Node;

/**
 * Fight transformer for the LW rulesystem.
 * @author Tamas_Szekeres
 */
public class LwFightTransformer extends ComplexFightTransformer<LwEnemy, LwFightCommand> {

    @Override
    void parseRulesetSpecificAttributes(final LwFightCommand fightCommand, final Node node) {
    }

    @Override
    LwFightCommand getFightCommandBean() {
        return getBeanFactory().getBean(LwFightCommand.class);
    }

}
