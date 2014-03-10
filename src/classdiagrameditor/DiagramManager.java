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

public class DiagramManager {
    // Complete list of elements in this diagram
    private List<Element> diagramModel_ = new LinkedList<Element>();

    // Set of elements that are currently selected
    private final Set<Element> selection_ = new TreeSet<Element>();
    
    private Momento momentoInstance_;
    
    /**
     * Draw all of the elements of this diagram
     * @param graphics graphics context
     */
    public void draw(Graphics2D graphics) {
        DrawElementVisitor v = new DrawElementVisitor(this, graphics);

        graphics.setColor(Color.BLACK);
        for (Element e : Lists.reverse(diagramModel_)) {
            e.accept(v);
        }
    }

    /**
     * Create a blank class diagram element
     * @param pos initial position
     */
    public void createClass(Point pos) {
        saveLastAction();
        ClassElement e = new ClassElement(pos);
        diagramModel_.add(0, e);
    }

    /**
     * Create a blank relationship diagram element
     * @param src Source element
     * @param dest Destination element
     */
    public void createRelationship(ClassElement src, ClassElement dest) {
        saveLastAction();
        RelationshipElement e = new RelationshipElement(src, dest);
        diagramModel_.add(0, e);
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
        for (Element e : diagramModel_) {
            if(e.contains(point)) {
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
        for (Element e : diagramModel_) {
            if(e.intersects(rectangle)) {
                selection_.add(e);
            }
        }
    }

    /**
     * Translate all selected elements
     * @param dx x offset of translation
     * @param dy y offset of translation
     */
    public void dragSelection(Point start, Point end, int dx, int dy) {
        boolean multiSelect = selection_.size() > 1;
        for (Element e : selection_) {
            e.drag(multiSelect, start, end, dx, dy);
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
            if(e.contains(point)) return true;
        }
        return false;
    }

    public boolean isSelected(Element element) {
        return selection_.contains(element);
    }

    /**
     * Find the element at a given position in the diagram
     * @param point position in the diagram
     * @return element if found or null
     */
    public Element findElementByPos(Point point) {
        for (Element e : diagramModel_) {
            if(e.contains(point)) return e;
        }
        return null;
    }
    
    /**
     * getMomentoInstance - returns a valid pointer to the momento singleton class
     * @return momento singleton instance
     */
    public Momento getMomentoInstance() {
        if (momentoInstance_ == null)
            momentoInstance_ = Momento.getInstance();
        
        return momentoInstance_;
    }
    
    /**
     * undoLastAction - used to perform undoing the last action performed on the
     *   elements list
     */
    public void undoLastAction() {
        getMomentoInstance().setState(diagramModel_, Momento.MomentoActionList.REDO);
        diagramModel_ = getMomentoInstance().getState(Momento.MomentoActionList.UNDO);
    }
    
     /**
     * redoLastAction - used to perform redoing the last action performed on the
     *   elements list (normally used for undoing an undo action)
     */
    public void redoLastAction() {
        getMomentoInstance().setState(diagramModel_, Momento.MomentoActionList.UNDO);
        diagramModel_ = getMomentoInstance().getState(Momento.MomentoActionList.REDO);
    }
    
    /**
     * saveState - save the current state of the elements_ to perform undo/redo
     *   actions in the future
     */
    public void saveLastAction() {
        getMomentoInstance().setState(diagramModel_, Momento.MomentoActionList.UNDO);
    }
}