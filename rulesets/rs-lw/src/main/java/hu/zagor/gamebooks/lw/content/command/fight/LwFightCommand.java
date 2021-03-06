package hu.zagor.gamebooks.lw.content.command.fight;

import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Fight command object for the LW rulesystem.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class LwFightCommand extends ComplexFightCommand<LwEnemy> {
    @Override
    public CommandView getCommandView(final String rulesetPrefix) {
        final Map<String, Object> model = new HashMap<>();
        model.put("fightCommand", this);
        if (isOngoing()) {
            hideChoices(model);
        }

        return new CommandView(rulesetPrefix + "Fightsingle", model);
    }

}
