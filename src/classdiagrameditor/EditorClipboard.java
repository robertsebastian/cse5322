package classdiagrameditor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EditorClipboard {
    private static EditorClipboard instance_;

    public static EditorClipboard getInstance() {
        if (instance_ == null) instance_ = new EditorClipboard();
        return instance_;
    }

    private final List<Element> elements_ = new LinkedList<Element>();

    public void set(Iterable<Element> elements) {
        elements_.clear();

        // Save off copy so it isn't modified while we're storing it
        for (Element e: elements) {
            elements_.add(e.makeCopy());
        }
    }

    public Collection<Element> insert(DiagramModel model) {
        LinkedList<Element> newElements = new LinkedList<Element>();
            
        // Map old IDs to new ones (might get re-inserted into same graph, so need fresh IDs)
        Map<Long, Long> idMap = new TreeMap<Long, Long>();
        for (Element e: elements_) {
            Element newElement = e.makeCopy();
            newElements.add(newElement);

            idMap.put(e.getId(), newElement.generateNewId());
        }

        // Add elements to model
        LinkedList<Element> addedElements = new LinkedList<Element>();
        for (Element e: newElements) {
            if (e instanceof LineConnectorElement) {
                // Remap source and dest if this is a connector element
                LineConnectorElement l = (LineConnectorElement)e;

                if (!idMap.containsKey(l.getDestId()) || !idMap.containsKey(l.getSourceId())) {
                    continue;
                }

                l.setDest(idMap.get(l.getDestId()));
                l.setSource(idMap.get(l.getSourceId()));
            }

            e.setModel(model);
            model.add(e);
            addedElements.add(e);
        }

        return addedElements;
    }

    public void clear() {
        elements_.clear();
    }
}
