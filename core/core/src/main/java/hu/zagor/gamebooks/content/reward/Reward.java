package hu.zagor.gamebooks.content.reward;

/**
 * Class containing information about a specific reward to be given to the player.
 * @author Tamas_Szekeres
 */
public class Reward {

    private String id;
    private boolean seriesId;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isSeriesId() {
        return seriesId;
    }

    public void setSeriesId(final boolean seriesId) {
        this.seriesId = seriesId;
    }

}
