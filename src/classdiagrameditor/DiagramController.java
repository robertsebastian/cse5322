package classdiagrameditor;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DiagramController {
    // Complete list of elements in this diagram
    private final List<Element> elements_ = new LinkedList<Element>();

    // Set of elements that are currently selected
    private final Set<Element> selection_ = new TreeSet<Element>();

    private int gridSpacing_ = 10;
    private long lastId_ = 0;

    /**
     * Draw all of the elements of this diagram
     * @param graphics graphics context
     */
    public void draw(Graphics2D graphics) {
        for (Element e : Lists.reverse(elements_)) {
            // Draw element
            graphics.setColor(Color.BLACK);
            e.draw(graphics, selection_.contains(e));
        }
    }

    /**
     * Create a blank class diagram element
     * @param pos initial position
     */
    public void createClass(Point pos) {
        ClassElement e = new ClassElement(getUniqueId(), pos);
        elements_.add(0, e);
    }

    /**
     * Create a blank relationship diagram element
     * @param pos1 position of source element
     * @param pos2 position of dest element
     */
    public void createRelationship(Point pos1, Point pos2) {
        Element src = findElementByPos(pos1);
        Element dest = findElementByPos(pos2);

        if(src == null || dest == null) return;

        AnchorElement a1 = new AnchorElement(getUniqueId(), src);
        AnchorElement a2 = new AnchorElement(getUniqueId(), dest);
        RelationshipElement e = new RelationshipElement(getUniqueId(), a1, a2);

        elements_.add(0, e);
        elements_.add(0, a1);
        elements_.add(0, a2);
    }

    /**
     * Clear the current selection
     */
    public void clearSelection() {
        selection_.clear();
    }

    /**
     * Add an element to the current selection
     * @param point  position of element to add
     * @param toggle if true, deselect if already selected
     */
    public void addSelection(Point point, boolean toggle) {
        for (Element e : elements_) {
            if(e.getBounds().contains(point)) {
                if(toggle && selection_.contains(e)) {
                    selection_.remove(e);
                } else {
                    selection_.add(e);
                }
                break;
            }
        }
    }

    /**
     * Add all elements that intersect with a rectangle to the selection
     * @param rectangle selection area
     */
    public void addSelection(Rectangle rectangle) {
        for (Element e : elements_) {
            if(e.getBounds().intersects(rectangle)) {
                selection_.add(e);
            }
        }
    }

    /**
     * Translate all selected elements
     * @param dx x offset of translation
     * @param dy y offset of translation
     */
    public void dragSelection(Point point, int dx, int dy) {
        for (Element e : selection_) {
            e.drag(point, dx, dy);
        }
    }

    public void dropSelection(Point point) {
        for (Element e : selection_) {
            e.drop(point);
        }
    }

    /**
     * Determine if a selected element is at point
     * @param point point in diagram
     * @return true if point is within a selected element
     */
    public boolean isPointInSelection(Point point) {
        for (Element e : selection_) {
            if(e.getBounds().contains(point)) return true;
        }
        return false;
    }

    /**
     * Find the element at a given position in the diagram
     * @param point position in the diagram
     * @return element if found or null
     */
    public Element findElementByPos(Point point) {
        for (Element e : elements_) {
            if(e.getBounds().contains(point)) return e;
        }
        return null;
    }

    /**
     * Get the next largest ID number in the diagram
     * @return unique ID number
     */
    private long getUniqueId() {
        for (Element e : elements_) {
            lastId_ = Math.max(e.getId(), lastId_);
        }
        lastId_ = lastId_ + 1; // Use at least one higher than largest id
        return lastId_;
    }

}