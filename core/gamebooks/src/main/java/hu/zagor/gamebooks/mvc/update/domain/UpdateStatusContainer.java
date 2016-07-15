package hu.zagor.gamebooks.mvc.update.domain;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * Class for storing the next update time.
 * @author Tamas_Szekeres
 */
@Component
public class UpdateStatusContainer {
    private DateTime updateTime;

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final DateTime updateTime) {
        this.updateTime = updateTime;
    }

}
