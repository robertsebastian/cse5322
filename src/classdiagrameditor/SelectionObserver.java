package classdiagrameditor;

import java.util.Set;

public interface SelectionObserver {
    public void notifySelectionChanged(DiagramManager manager, Set<Element> selection);
}
