package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.enemy.LwEnemy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Fight command object for the LW rulesystem.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class LwFightCommand extends ComplexFightCommand<LwEnemy> {

}
