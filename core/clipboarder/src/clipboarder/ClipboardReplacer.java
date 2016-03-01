package clipboarder;

public class ClipboardReplacer {

    public static void main(final String[] args) {
        final ClipboardListener clipboardListener = new ClipboardListener();
        clipboardListener.start();
    }

}
