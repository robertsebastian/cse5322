package classdiagrameditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Element
    implements Comparable {

    // Keep track of unique ID for each element
    private static AtomicLong idGenerator_ = new AtomicLong();

    private final long id_; // Unique identifier
    private Set<Element> observers_;

    public Element() {
        id_ = idGenerator_.getAndIncrement();
    }

    public abstract void drag(boolean multiSelect, Point start, Point end, int dx, int dy);
    public abstract void drop(Point point);
    public abstract boolean contains(Point point);
    public abstract boolean intersects(Rectangle rectangle);

    @Override
    public int compareTo(Object o) {
        return Long.compare(id_, ((Element)o).id_);
    }

    public long getId() {return id_;}

    public void registerObserver(Element e) {
        if (observers_ == null) observers_ = new TreeSet<Element>();
        observers_.add(e);
    }

    public void unregisterObserver(Element e) {
        if (observers_ == null) return;
        observers_.remove(e);
    }

    public void notifyObservers() {
        if (observers_ == null) return;
        for (Element e: observers_) e.notifyChanged(this);
    }

    public void notifyChanged(Element e) {}

    public void accept(ElementVisitor elementVisitor) {
        accept(elementVisitor);
    }
}