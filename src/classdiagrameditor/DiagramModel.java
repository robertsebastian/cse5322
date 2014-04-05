package classdiagrameditor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiagramModel implements Iterable<Element> {
    private final List<Element> elements_ = new LinkedList<Element>();
    private final Map<Long, Element> byId_ = new TreeMap<Long, Element>();

    public List<Element> getElements() {return elements_;}

    public Element find(Element e) {
        return find(e.getId());
    }

    public Element find(long id) {
        return byId_.get(id);
    }

    public void add(Element e) {
        add(e, false);
    }

    public void add(Element e, boolean bottom) {
        e.setModel(this);

        elements_.add(bottom ? elements_.size() : 0, e);
        byId_.put(e.getId(), e);
    }

    public Collection<Element> addCopy(Iterable<Element> elements) {
        LinkedList<Element> newElements = new LinkedList<Element>();

        // Map old IDs to new ones (might be re-insertint into same model, so need fresh IDs)
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

                // Don't add dangling relationships
                if (!idMap.containsKey(l.getDestId()) || !idMap.containsKey(l.getSourceId())) {
                    continue;
                }

                l.setDest(idMap.get(l.getDestId()));
                l.setSource(idMap.get(l.getSourceId()));
            }
        }

        return addedElements;
    }
            

    public void delete(Element e) {
        delete(e.getId());
    }

    public void delete(long id) {
        Element e = byId_.get(id);
        elements_.remove(e);
        byId_.remove(id);
    }

    @Override
    public Iterator<Element> iterator() {
        return elements_.iterator();
    }

    // Create a memento with a copy of this diagram model's state
    public DiagramModelMemento createMemento() {
        DiagramModelMemento m = new DiagramModelMemento();
        m.setState(this);

        return m;
    }

    public void setMemento(DiagramModelMemento memento) {
        elements_.clear();
        byId_.clear();

        // Add all elements saved in memento
        for (Element e : memento.getState()) {
            add(e);
        }
    }
    
    public void deleteModels() {
        elements_.clear();
        byId_.clear();
    }
    
    public int elementCount() { return elements_.size();}
}
