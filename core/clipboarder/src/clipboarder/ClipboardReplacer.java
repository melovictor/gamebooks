package clipboarder;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClipboardReplacer {
    private static final Pattern IMAGE_NAME_PATTERN = Pattern.compile("b-([0-9]+)\\.jpg");
    private static final Toolkit DEFAULT_TOOLKIT = Toolkit.getDefaultToolkit();

    private final Replacer hunReplacer = new HungarianReplacer();
    // private final Replacer altHunReplacer = new AlternateHungarianReplacer();
    private final Replacer engReplacer = new EnglishReplacer();
    private final ContentSorter contentSorter = new ContentSorter();

    private final Pattern paragraphPattern = Pattern.compile("([^\\n\\r]+)");

    private String lastString = "";
    private final Clipboard clipboard = DEFAULT_TOOLKIT.getSystemClipboard();

    private BookImageData imageData;

    public static void main(final String[] args) throws InterruptedException {
        final ClipboardReplacer clipboardReplacer = new ClipboardReplacer();
        while (true) {
            try {
                clipboardReplacer.checkClipboard();
            } catch (final Exception ex) {
            }
            Thread.sleep(100);
        }
    }

    public void checkClipboard() throws UnsupportedFlavorException, IOException {
        if (!clipboard.getContents(null).isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            final Reader clipboardReader = DataFlavor.stringFlavor.getReaderForText(clipboard.getContents(null));

            final Scanner scanner = new Scanner(clipboardReader);
            String clipboardContent = "";

            while (scanner.hasNextLine()) {
                clipboardContent += scanner.nextLine() + "\n";
            }

            scanner.close();
            clipboardContent = clipboardContent.replace("¬", "").trim();
            if (lastString == null || !lastString.equals(clipboardContent)) {
                if (!isConverted(clipboardContent) && !isProgramCode(clipboardContent)) {
                    processClipboardContent(clipboardContent);
                } else {
                    final String newContent = contentSorter.tryMap(clipboardContent, imageData);
                    if (newContent != null) {
                        clipboardContent = newContent;
                        lastString = clipboardContent;
                        final StringSelection selection = new StringSelection(newContent);
                        clipboard.setContents(selection, selection);
                        System.exit(0);
                    } else {
                        lastString = clipboardContent;
                    }
                }
            }
        } else {
            @SuppressWarnings("unchecked")
            final List<File> transferData = (List<File>) clipboard.getContents(null).getTransferData(DataFlavor.javaFileListFlavor);
            imageData = new BookImageData();
            imageData.setCode(transferData.get(0).getParentFile().getName());
            for (final File file : transferData) {
                final String filename = file.getName();
                final Matcher matcher = IMAGE_NAME_PATTERN.matcher(filename);
                if (matcher.matches()) {
                    imageData.getSections().add(matcher.group(1));
                }
            }
            System.out.println("Image file list initialized");
            final StringSelection emptyString = new StringSelection("");
            clipboard.setContents(emptyString, emptyString);
        }
    }

    private boolean isProgramCode(final String clipboardContent) {
        return clipboardContent.contains("{") || clipboardContent.contains("<") || clipboardContent.contains(">")
            || (clipboardContent.contains(";") && !clipboardContent.contains("; "));
    }

    private boolean isConverted(final String clipboardContent) {
        return clipboardContent.contains("[p]") || clipboardContent.contains("[li]");
    }

    private void processClipboardContent(final String content) {
        final String newContent = convertParagraphs(content);
        lastString = newContent;
        final StringSelection selection = new StringSelection(newContent);
        clipboard.setContents(selection, selection);
    }

    private String convertParagraphs(final String content) {
        String newContent = null;

        newContent = hunReplacer.tryMap(content);
        if (newContent == null) {
            newContent = engReplacer.tryMap(content);
        }
        if (newContent == null) {
            newContent = contentSorter.tryMap(content, imageData);
        }

        if (newContent == null) {
            newContent = "";
            if (content.contains("\n")) {
                final Matcher matcher = paragraphPattern.matcher(content);
                while (matcher.find()) {
                    newContent += "\t\t[p]" + matcher.group(0) + "[/p]\n";
                }
                newContent = newContent.trim();
            } else {
                newContent = content;
            }
        }

        System.out.println("All targeted conversion failed, fallback on standard conversion:");
        System.out.println(newContent);
        return newContent;
    }

}
