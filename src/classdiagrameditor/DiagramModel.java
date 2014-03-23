package classdiagrameditor;

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
}
