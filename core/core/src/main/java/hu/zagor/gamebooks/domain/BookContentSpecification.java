package hu.zagor.gamebooks.domain;

/**
 * Bean for storing adventure-specific information about the given book like whether it has inventory or map, etc.
 * @author Tamas_Szekeres
 */
public class BookContentSpecification {

    private boolean inventoryAvailable;
    private boolean mapAvailable;
    private int characterBackpackSize = Integer.MAX_VALUE;

    public boolean isInventoryAvailable() {
        return inventoryAvailable;
    }

    public void setInventoryAvailable(final boolean inventoryAvailable) {
        this.inventoryAvailable = inventoryAvailable;
    }

    public boolean isMapAvailable() {
        return mapAvailable;
    }

    public void setMapAvailable(final boolean mapAvailable) {
        this.mapAvailable = mapAvailable;
    }

    public int getCharacterBackpackSize() {
        return characterBackpackSize;
    }

    public void setCharacterBackpackSize(final int characterBackpackSize) {
        this.characterBackpackSize = characterBackpackSize;
    }

}
