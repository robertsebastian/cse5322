package classdiagrameditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicLong;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract class Element
    implements Comparable {

    // Keep track of unique ID for each element
    private static AtomicLong idGenerator_ = new AtomicLong();

    private long id_;               // Unique identifier
    protected DiagramModel model_;  // Currently associated diagram state object

    public Element() {
        id_ = idGenerator_.getAndIncrement();
    }

    public Element(long id) {
        id_ = id;
    }

    public Element(Element element) {
        id_    = element.id_;
        model_ = element.model_;
    }

    public long generateNewId() {
        id_ = idGenerator_.getAndIncrement();
        return id_;
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
     * Set this object's unique identifier.
     * @param newID of this object
     */
    public void setID(long newID) {
        id_ = newID;
    }
    
    /**
     * Get this object's unique identifier.
     * @return ID of this object
     */
    public long getId() {
        return id_;
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

    public abstract double[][] getAnchorPoints();
    public abstract int getClosestAnchorPoint(Point p);
    public abstract boolean getAnchorPoint(Point target, int i);

    public abstract void readXML(XMLStreamReader reader_) throws XMLStreamException;
}