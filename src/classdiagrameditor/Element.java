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

    private final long id_;                  // Unique identifier
    private Set<ElementObserver> observers_; // Currently registered observers
    protected DiagramModel model_;           // Currently associated diagram state object

    public Element() {
        id_ = idGenerator_.getAndIncrement();
    }

    public Element(Element element) {
        id_    = element.id_;
        model_ = element.model_;
    }

    @Override
    public int compareTo(Object o) {
        // Just compare element IDs
        return Long.compare(id_, ((Element)o).id_);
    }

    /**
     * Called for each mouse position while this element is being dragged.
     * @param multiSelect True if other elements are also selected in this drag
     * @param start       Point where drag started (initial mouse press position)
     * @param end         Current position of mouse pointer
     * @param dx          Change in x position since last call to drag(...)
     * @param dy          Change in y position since last call to drag(...)
     */
    public abstract void drag(boolean multiSelect, Point start, Point end, int dx, int dy);

    /**
     * Called when mouse is released after a drag.
     * @param point Final point where mouse was released
     */
    public abstract void drop(Point point);

    /**
     * Invoke the copy constructor with this object and return a new instance.
     * @return New element with copied state
     */
    public abstract Element makeCopy();

    /**
     * Determine if this element contains point.
     * @param point Point to check against element boundaries
     * @return True if the element contains point
     */
    public abstract boolean contains(Point point);

    /**
     * Determine if this element is intersected by a rectangle.
     * @param rectangle Rectangle to check against element boundaries
     * @return True if rectangle intersects with the boundary of this object
     */
    public abstract boolean intersects(Rectangle rectangle);

    /**
     * Get this object's unique identifier.
     * @return ID of this object
     */
    public long getId() {
        return id_;
    }

    /**
     * Register a new observer.
     * @param observer Object that will be notified of state updates
     */
    public void registerObserver(ElementObserver observer) {
        if (observers_ == null) observers_ = new TreeSet<ElementObserver>();
        observers_.add(observer);
    }

    /**
     * Unregister an existing observer.
     * @param observer Object to remove from observer list
     */
    public void unregisterObserver(ElementObserver observer) {
        if (observers_ == null) return;
        observers_.remove(observer);
    }

    /**
     * Notify all registered observers that a change in state has taken place
     */
    public void notifyObservers() {
        if (observers_ == null) return;
        for (ElementObserver e: observers_) e.notifyElementChanged(this);
    }

    /**
     * Get list of observers.
     * @return Iterable representation of observer list
     */
    public Iterable<ElementObserver> getObservers() {
        return observers_;
    }

    /**
     * Call the visitor's visit method for this class type.
     * @param elementVisitor visitor
     */
    public abstract void accept(ElementVisitor elementVisitor);

    /**
     * Update the currently associated diagram for this element.
     * 
     * @param model Model that contains this element
     */
    public void setModel(DiagramModel model) {model_ = model;}
}