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

public class DiagramModel {
    // Complete list of elements in this diagram
    private final List<DiagramElement> elements_ = new LinkedList<DiagramElement>();

    // Set of elements that are currently selected
    private final Set<DiagramElement> selection_ = new TreeSet<DiagramElement>();

    private int gridSpacing_ = 10;

    /**
     * Draw all of the elements of this diagram
     * @param graphics graphics context
     */
    public void draw(Graphics2D graphics) {
        for (DiagramElement e : Lists.reverse(elements_)) {
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
        ClassDiagramElement e = new ClassDiagramElement(getUniqueId(), pos);
        elements_.add(0, e);
    }

    /**
     * Create a blank relationship diagram element
     * @param pos1 position of source element
     * @param pos2 position of dest element
     */
    public void createRelationship(Point pos1, Point pos2) {
        DiagramElement src = findElementByPos(pos1);
        DiagramElement dest = findElementByPos(pos2);

        if(src == null || dest == null) return;

        RelationshipDiagramElement e = new RelationshipDiagramElement(getUniqueId(), src, dest);
        elements_.add(0, e);
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
        for (DiagramElement e : elements_) {
            if(e.getShape().contains(point)) {
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
        for (DiagramElement e : elements_) {
            if(e.getShape().intersects(rectangle)) {
                selection_.add(e);
            }
        }
    }

    /**
     * Translate all selected elements
     * @param dx x offset of translation
     * @param dy y offset of translation
     */
    public void moveSelection(int dx, int dy) {
        for (DiagramElement e : selection_) {
            e.translate(dx, dy);
        }
    }

    /**
     * Determine if a selected element is at point
     * @param point point in diagram
     * @return true if point is within a selected element
     */
    public boolean isPointInSelection(Point point) {
        for (DiagramElement e : selection_) {
            if(e.getShape().contains(point)) return true;
        }
        return false;
    }

    /**
     * Find the element at a given position in the diagram
     * @param point position in the diagram
     * @return element if found or null
     */
    public DiagramElement findElementByPos(Point point) {
        for (DiagramElement e : elements_) {
            if(e.getShape().contains(point)) return e;
        }
        return null;
    }

    /**
     * Get the next largest ID number in the diagram
     * @return unique ID number
     */
    private long getUniqueId() {
        long newId = 0;
        for (DiagramElement e : elements_) {
            newId = Math.max(e.getId(), newId);
        }
        return newId + 1;
    }

}