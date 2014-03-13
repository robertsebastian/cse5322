package classdiagrameditor;

import java.util.Set;

public interface SelectionObserver {
    public void notifySelectionChanged(Set<Element> selection);
}
