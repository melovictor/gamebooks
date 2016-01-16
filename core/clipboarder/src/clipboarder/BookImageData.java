package clipboarder;

import java.util.ArrayList;
import java.util.List;

public class BookImageData {
    private String code;
    private final List<String> sections = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public List<String> getSections() {
        return sections;
    }
}
