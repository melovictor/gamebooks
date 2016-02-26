package hu.zagor.gamebooks.player;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Object for storing a reward already earned by the player.
 * @author Tamas_Szekeres
 */
@XmlType
public class Reward {
    private long bookId;
    private String code;

    @XmlAttribute
    public long getBookId() {
        return bookId;
    }

    public void setBookId(final long bookId) {
        this.bookId = bookId;
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}
