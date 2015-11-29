package hu.zagor.gamebooks.ff.character;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Player character for Sorcery ruleset.
 * @author Tamas_Szekeres
 */
@Component("sorCharacter")
@Scope("prototype")
public class SorCharacter extends FfCharacter {
    private boolean wizard;
    private boolean usedLibra;

    public boolean isWizard() {
        return wizard;
    }

    public void setWizard(final boolean wizard) {
        this.wizard = wizard;
    }

    public boolean isUsedLibra() {
        return usedLibra;
    }

    public void setUsedLibra(final boolean usedLibra) {
        this.usedLibra = usedLibra;
    }

}
