package classdiagrameditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Element
    implements Comparable {

    public static AtomicLong idGenerator_ = new AtomicLong();

    protected Point location_;

    private final long id_; // Unique identifier
    private Shape bounds_;  // Bounding box for determining selection
    private boolean isDragging_ = false;
    private Set<Element> observers_;

    public Element() {
        this(new Point());
    }

    public Element(Point pos) {
        id_       = idGenerator_.getAndIncrement();
        location_ = new Point(pos);
        bounds_   = new Rectangle(pos.x, pos.y, 100, 100);
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(id_, ((Element)o).id_);
    }

    public long getId() {return id_;}

    public Point getLocation() {return location_;}

    public Shape getBounds() {return bounds_;}
    public void setBounds(Shape shape) {bounds_ = shape;}
    public boolean isDragging() {return isDragging_;}

    public void drag(Point point, int dx, int dy) {
        isDragging_ = true;
    }

    public void drop(Point point) {
        isDragging_ = false;
    }

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

    public abstract void accept(ElementVisitor elementVisitor);

    //public boolean intersects
            // If already selected, check sub-selection of control points and route drags to it?
}