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

    /**
     * Draw all of the elements of this diagram
     * @param graphics graphics context
     */
    public void draw(Graphics2D graphics) {
        for (DiagramElement e : Lists.reverse(elements_)) {
            // Draw element
            graphics.setColor(Color.BLACK);
            e.draw(graphics);

            // Draw selection highlight if selected
            if (selection_.contains(e)) {
                graphics.setColor(new Color(0.2f, 0.2f, 1.0f, 0.5f));
                Rectangle bounds = e.getBounds();
                graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    /**
     * Create a blank class diagram element
     * @param pos initial position
     */
    public void createClass(Point pos) {
        DiagramElement e = new ClassDiagramElement();
        e.getBounds().setLocation(pos);
        e.setId(getUniqueId());
        
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
        for (DiagramElement e : elements_) {
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
    public void moveSelection(int dx, int dy) {
        for (DiagramElement e : selection_) {
            e.getBounds().translate(dx, dy);
        }
    }

    /**
     * Determine if a selected element is at point
     * @param point point in diagram
     * @return true if point is within a selected element
     */
    public boolean isPointInSelection(Point point) {
        for (DiagramElement e : selection_) {
            if(e.getBounds().contains(point)) return true;
        }
        return false;
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