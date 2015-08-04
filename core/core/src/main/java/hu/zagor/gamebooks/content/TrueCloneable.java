package hu.zagor.gamebooks.content;

import java.io.Serializable;

/**
 * Interface for a true cloneable bean that has a public clone method.
 * @author Tamas_Szekeres
 */
public interface TrueCloneable extends Cloneable, Serializable {

    /**
     * Clones the bean.
     * @return the cloned bean
     * @throws CloneNotSupportedException occurs if there is a problem during the cloning process
     */
    TrueCloneable clone() throws CloneNotSupportedException;
}
