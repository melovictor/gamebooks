package hu.zagor.gamebooks.mvc.logs.domain;

/**
 * Bean for storing information about a specific log file.
 * @author Tamas_Szekeres
 */
public class LogFileData implements Comparable<LogFileData> {

    private String timestamp;
    private String userId;
    private String userName;
    private String loginDateTime;
    private String bookId;
    private String bookName;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(final String loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public String getName() {
        return "log-" + userId + (timestamp == null ? "" : "-" + timestamp) + ".log";
    }

    @Override
    public int compareTo(final LogFileData o) {
        int compareResult = timestamp.compareTo(o.timestamp);
        if (compareResult == 0) {
            if (bookId == null) {
                compareResult = o.bookId == null ? 0 : -1;
            } else {
                compareResult = o.bookId == null ? 1 : bookId.compareTo(o.bookId);
            }
        }
        return compareResult;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(final String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(final String bookName) {
        this.bookName = bookName;
    }

}
