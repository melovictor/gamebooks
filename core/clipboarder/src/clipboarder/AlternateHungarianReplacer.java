package clipboarder;

public class AlternateHungarianReplacer implements Replacer {

    @Override
    public String tryMap(final String content) {

        String newContent = content;
        if (content.contains("\n")) {
            final String[] lines = content.split("[\\r\\n]");
            final int length = lines.length;

            final String sectionId = getSectionId(lines);

            if (sectionId != null) {
                newContent = convertFullSection(lines, length, sectionId);
            } else {
                newContent = convertPartialSectionContent(lines);
            }

        } else {
            newContent = content;
        }

        return newContent;
    }

    private String convertPartialSectionContent(final String[] lines) {
        String newContent;
        newContent = "";
        for (final String line : lines) {
            if (line.contains("apozz a")) {
                int idx = line.indexOf(", lapozz a");
                if (idx < 0) {
                    idx = line.indexOf(" lapozz a");
                }
                final String text = line.substring(0, idx);
                final String target = line.substring(idx).replaceAll(".*?([0-9]+).*", "$1");
                newContent += "    <next id=\"" + target + "\">" + text.trim() + "</next>\n";
            } else {
                newContent += "        [p]" + line + "[/p]\n";
            }
        }
        newContent = newContent.trim();
        return newContent;
    }

    private String convertFullSection(final String[] lines, final int length, final String sectionId) {
        String newContent;
        newContent = "<p id=\"" + sectionId + "\">\n    <text>";
        boolean closedText = false;
        for (int i = 1; i < length; i++) {
            final String line = lines[i];
            if (line.contains("apozz a")) {
                if (line.contains("Lapozz a")) {
                    final int idx = line.indexOf("Lapozz a");
                    final String actualContent = line.substring(0, idx).trim();
                    final String target = line.substring(idx).replaceAll(".*?([0-9]+).*", "$1");
                    newContent += "\n        [p]" + actualContent + "[/p]";
                    newContent += "\n    </text>\\n\\t<next id=\"" + target + "></next>";
                } else {
                    int idx = line.indexOf(", lapozz a");
                    if (idx < 0) {
                        idx = line.indexOf(" lapozz a");
                    }
                    final String text = line.substring(0, idx);
                    final String target = line.substring(idx).replaceAll(".*?([0-9]+).*", "$1");
                    if (!closedText) {
                        newContent += "\n    </text>";
                        closedText = true;
                    }
                    newContent += "\n    <next id=\"" + target + "\">" + text.trim() + "</next>";
                }
            } else {
                newContent += "\n        [p]" + line + "[/p]";
            }
        }
        if (!closedText) {
            newContent += "\n    </text>";
        }
        newContent += "\n  </p>";

        newContent = newContent.trim();
        return newContent;
    }

    private String getSectionId(final String[] lines) {
        final String id = lines[0].replace(".", "");
        String sectionId;
        try {
            Integer.valueOf(id);
            sectionId = id;
        } catch (final NumberFormatException ex) {
            sectionId = null;
        }

        return sectionId;
    }
}
