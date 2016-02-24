package hu.zagor.gamebooks.mvc.logs.domain;

import java.util.Set;
import java.util.TreeSet;

/**
 * Bean for storing information about all saved games that belong to a specific player.
 * @author Tamas_Szekeres
 */
public class SaveFileContainer implements Comparable<SaveFileContainer> {
    private String userId;
    private String userName;

    private final Set<SavedFileData> savedFiles = new TreeSet<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName == null ? userId : userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public Set<SavedFileData> getSavedFiles() {
        return savedFiles;
    }

    @Override
    public int compareTo(final SaveFileContainer o) {
        return getUserName().compareTo(o.getUserName());
    }

}
