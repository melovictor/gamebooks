package hu.zagor.gamebooks.character.enemy;

import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Base class for enemies.
 * @author Tamas_Szekeres
 */
public class Enemy implements TrueCloneable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Enemy clone() throws CloneNotSupportedException {
        return (Enemy) super.clone();
    }

}
