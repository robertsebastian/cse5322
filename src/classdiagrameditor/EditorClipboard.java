package classdiagrameditor;

import java.util.LinkedList;
import java.util.List;

public class EditorClipboard {
    private static EditorClipboard instance_;

    public static EditorClipboard getInstance() {
        if (instance_ == null) instance_ = new EditorClipboard();
        return instance_;
    }

    private final List<Element> elements_ = new LinkedList<Element>();

    public void setContents(Iterable<Element> elements) {
        elements_.clear();

        // Save off copy so it isn't modified while we're storing it
        for (Element e: elements) {
            elements_.add(e.makeCopy());
        }
    }

    public List<Element> getContents() {
        return elements_;
    }
}
