package hu.zagor.gamebooks.lw.character;

import hu.zagor.gamebooks.character.Character;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("lwCharacter")
@Scope("prototype")
public class LwCharacter extends Character {

}
