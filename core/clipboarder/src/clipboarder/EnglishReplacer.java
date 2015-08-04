package clipboarder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnglishReplacer implements Replacer {

    private static final String TURNTO = "(?:,| –) turn to ([0-9]+).";
    private static final String BIG_TURNTO = "Turn to ([0-9]+).";
    private final Pattern multiChoiceSectionPattern = Pattern.compile("([0-9]+)\\s+(.*[?!.–]) (.*)" + TURNTO + " (.*) – turn to ([0-9]+).(?: (.*)" + TURNTO
        + "){0,1}(?: (.*)" + TURNTO + "){0,1}(?: (.*)" + TURNTO + "){0,1}(?: (.*)" + TURNTO + "){0,1}(?: (.*)" + TURNTO + "){0,1}");
    private final Pattern singleChoiceLikeMultiSectionPattern = Pattern.compile("([0-9]+)\\s+(.*[?!.–]) (.*)" + TURNTO);
    private final Pattern singleSendoffSectionPattern = Pattern.compile("([0-9]+)\\s+(.*[?!.–]) " + BIG_TURNTO);
    private final Pattern multiChoiceListSectionPattern = Pattern.compile("([0-9]+)\\s+(.*[?!.])[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "(?:[\\r\\n]+(.*?)[ \\t]+"
        + BIG_TURNTO + "){0,1}(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "){0,1}(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "){0,1}(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO
        + "){0,1}(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "){0,1}(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "){0,1}(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "){0,1}");

    private final Pattern multiChoiceListSectionWithParenthesisPattern = Pattern
        .compile("([0-9]+)\\s+(.*\\.) ([^()]+)\\(turn to ([0-9]+)\\)(?:[,. ]*([^()]+)\\(turn to ([0-9]+)\\)){0,1}(?:[,. ]*([^()]+)\\(turn to ([0-9]+)\\)){0,1}(?:[,. ]*([^()]+)\\(turn to ([0-9]+)\\)){0,1}(?:[,. ]*([^()]+)\\(turn to ([0-9]+)\\)){0,1}(?:[,. ]*([^()]+)\\(turn to ([0-9]+)\\)){0,1}(?:[,. ]*([^()]+)\\(turn to ([0-9]+)\\)){0,1}.");
    private final Pattern multiChoiceContinuousSectionPattern = Pattern
        .compile("([0-9]+)\\s+(.*?\\.) ([^.?!]*?), turn to ([0-9]+). (.*?), turn to ([0-9]+).(?: (.*?), turn to ([0-9]+).){0,1}(?: (.*?), turn to ([0-9]+).){0,1}(?: (.*?), turn to ([0-9]+).){0,1}(?: (.*?), turn to ([0-9]+).){0,1}(?: (.*?), turn to ([0-9]+).){0,1}(?: (.*?), turn to ([0-9]+).){0,1}(?: (.*?), turn to ([0-9]+).){0,1}");

    private final Pattern dying = Pattern.compile("([0-9]+)\\s+(.*Your adventure ends here[!.])");

    private final Pattern choiceOnly = Pattern.compile("([^.]*), turn to ([0-9]+)\\.");

    @Override
    public String tryMap(final String content) {
        return tryFitPatterns(content);
    }

    private String tryFitPatterns(final String content) {
        String newContent = null;
        newContent = fitMcs(content);
        if (newContent == null) {
            newContent = fitSss(content);
        }
        if (newContent == null) {
            newContent = fitSclms(content);
        }
        if (newContent == null) {
            newContent = fitCo(content);
        }
        if (newContent == null) {
            newContent = fitD(content);
        }

        return newContent;
    }

    private String fitD(final String content) {
        String newContent = null;
        final Matcher matcher = dying.matcher(content);
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";
            newContent += "\n\t<death />";
            newContent += "\n  </p>";
            System.out.println("Converted based on fitD, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitCo(final String content) {
        String newContent = null;
        final Matcher matcher = choiceOnly.matcher(content);
        if (matcher.matches()) {
            newContent = "<next id=\"" + matcher.group(2).trim() + "\">" + matcher.group(1).trim() + "</next>";
            System.out.println("Converted based on fitCo, result:");
            System.out.println(newContent);
        }

        return newContent;
    }

    private String fitSclms(final String content) {
        String newContent = null;
        final Matcher matcher = singleChoiceLikeMultiSectionPattern.matcher(content);
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2) + " " + matcher.group(3) + ".";
            final String choiceId = matcher.group(4);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";
            newContent += "\n\t<next id=\"" + choiceId + "\"></next>";
            newContent += "\n  </p>";
            System.out.println("Converted based on fitSclms, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitSss(final String content) {
        String newContent = null;
        final Matcher matcher = singleSendoffSectionPattern.matcher(content);
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2);
            final String choiceId = matcher.group(3);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";
            newContent += "\n\t<next id=\"" + choiceId + "\"></next>";
            newContent += "\n  </p>";
            System.out.println("Converted based on fitSss, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitMcs(final String content) {
        String newContent = null;
        String matchPattern = "Mcs";
        Matcher matcher = multiChoiceSectionPattern.matcher(content);
        if (!matcher.matches()) {
            matcher = multiChoiceListSectionPattern.matcher(content);
            matchPattern = "Mcls";
        }
        if (!matcher.matches()) {
            matcher = multiChoiceListSectionWithParenthesisPattern.matcher(content);
            matchPattern = "Mclswpp";
        }
        if (!matcher.matches()) {
            matcher = multiChoiceContinuousSectionPattern.matcher(content);
            matchPattern = "Mccsp";
        }

        if (matcher.matches()) {
            final String id = matcher.group(1);
            String mainText = matcher.group(2);

            boolean isLuckTest = false;
            boolean isSkillTest = false;
            if (mainText.contains("Test your Luck.")) {
                mainText = mainText.replace("Test your Luck.", "").trim();
                isLuckTest = true;
            }
            if (mainText.contains("Test your Luck")) {
                mainText = mainText.replace("Test your Luck", "").trim();
                isLuckTest = true;
            }
            if (mainText.contains("Roll two dice") && (matcher.group(5).contains("than your SKILL score") || matcher.group(3).contains("than your SKILL score"))) {
                mainText = mainText.replace("Roll two dice.", "").replace("Roll two dice", "").trim();
                isSkillTest = true;
            }

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";
            if (isLuckTest) {
                newContent += "\n\t<test against=\"luck\">\n";
                newContent += "      <success>\n" + "        <next id=\"" + matcher.group(4) + "\"></next>\n" + "      </success>\n" + "      <failure>\n"
                    + "        <next id=\"" + matcher.group(6) + "\"></next>\n" + "      </failure>";
                newContent += "\n\t</test>";
            } else if (isSkillTest) {
                newContent += "\n\t<test against=\"skill\">\n";
                newContent += "      <success>\n" + "        <next id=\"" + matcher.group(4) + "\"></next>\n" + "      </success>\n" + "      <failure>\n"
                    + "        <next id=\"" + matcher.group(6) + "\"></next>\n" + "      </failure>";
                newContent += "\n\t</test>";
            } else {
                for (int idx = 3; idx < matcher.groupCount(); idx += 2) {
                    String choiceText = matcher.group(idx);
                    final String choiceId = matcher.group(idx + 1);
                    if (choiceText != null) {
                        choiceText = choiceText.trim();
                        choiceText = choiceText.substring(0, 1).toUpperCase() + choiceText.substring(1);
                        newContent += "\n\t<next id=\"" + choiceId + "\">" + choiceText + "</next>";
                    }
                }
            }

            newContent += "\n  </p>";

            System.out.println("Converted based on fit" + matchPattern + ", result:");
            System.out.println(newContent);
        }
        return newContent;
    }
}
