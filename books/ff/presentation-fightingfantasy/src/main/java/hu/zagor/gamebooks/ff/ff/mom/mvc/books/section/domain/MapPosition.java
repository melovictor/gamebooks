package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain;

/**
 * Bean for storing and retrieving a position of one of the parties.
 * @author Tamas_Szekeres
 */
public class MapPosition {

    private int x;
    private int y;

    /**
     * Creates a new MapPosition from a map coordinate (eg. E12).
     * @param position the map coordinate
     */
    public MapPosition(final String position) {
        final Character posX = position.charAt(0);
        y = Integer.parseInt(position.substring(1));
        x = posX + 1 - 'A';
    }

    public String getPosition() {
        return Character.toChars('A' - 1 + x)[0] + String.valueOf(y);
    }

    /**
     * Changes the Y position of the entity.
     * @param i the amount by which the position has to be changed
     */
    public void changeY(final int i) {
        y = y + i;
    }

    /**
     * Changes the X position of the entity.
     * @param i the amount by which the position has to be changed
     */
    public void changeX(final int i) {
        x = x + i;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
