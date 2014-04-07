package classdiagrameditor;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class DiagramManager {
    static DiagramManager instance_;

    // Complete list of elements in this diagram
    private final DiagramModel diagramModel_ = new DiagramModel();

    // Set of elements that are currently selected
    private final Set<Element> selection_ = new TreeSet<Element>();

    // Set of selection observer listeners
    private final List<SelectionObserver> selectionObservers_ = new LinkedList<SelectionObserver>();
    
    private static final int UNDO_STACK_MAX_SIZE = 10;
    private final LinkedList<DiagramModelMemento> undoStack_ = 
            new LinkedList<DiagramModelMemento>();
    DiagramModelMemento currentState_ = null;
    private int undoPos_ = -1;

    /**
     * Do any cleanup work when this diagram is being closed
     */
    public void close() {
        // Let selection observers know that the selection is no longer valid
        clearSelection();
    }
    
    /**
     * Draw all of the elements of this diagram
     * @param graphics graphics context
     */
    public void draw(Graphics2D graphics) {
        DrawElementVisitor v = new DrawElementVisitor(this, graphics);

        graphics.setColor(Color.BLACK);
        for (Element e : Lists.reverse(diagramModel_.getElements())) {
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
        diagramModel_.add(e);
    }

    /**
     * Create a blank relationship diagram element
     * @param src Source element
     * @param dest Destination element
     * @param pos Place to find anchor points relative to
     */
    public void createRelationship(Element src, Element dest, Point pos) {
        saveLastAction();
        RelationshipElement e = new RelationshipElement(src, dest, pos);
        diagramModel_.add(e);
    }

    public void cut() {
        EditorClipboard.getInstance().setContents(selection_);
        deleteSelection();
    }

    public void copy() {
        EditorClipboard.getInstance().setContents(selection_);
    }

    public void paste() {
        clearSelection();
        selection_.addAll(diagramModel_.addCopy(EditorClipboard.getInstance().getContents()));
    }

    /**
     * Clear the current selection
     */
    public void clearSelection() {
        selection_.clear();
        notifySelectionObservers();
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
        notifySelectionObservers();
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
        notifySelectionObservers();
    }

    /**
     * Tell all elements that they have been dragged
     * @param firstEvent True if this is the first drag event in this series
     * @param start Cursor position where drag started
     * @param end Current cursor position of drag
     * @param dx x offset of translation
     * @param dy y offset of translation
     */
    public void dragSelection(boolean firstEvent, Point start, Point end, int dx, int dy) {
        if (firstEvent) saveLastAction();

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
     * undoLastAction - used to perform undoing the last action performed on the
     *   elements list
     */
    public void undoLastAction() {
        if (undoPos_ > 0 && undoPos_ < undoStack_.size()) {
            // Save off current state if we're undoing to the top of the stack
            if(undoPos_ == undoStack_.size() - 1 && undoStack_.getLast() != currentState_) {
                saveLastAction();
                currentState_ = undoStack_.getLast();
            }

            undoPos_--;
            diagramModel_.setMemento(undoStack_.get(undoPos_));
        }
    }
    
     /**
     * redoLastAction - used to perform redoing the last action performed on the
     *   elements list (normally used for undoing an undo action)
     */
    public void redoLastAction() {
        if (undoPos_ >= 0 && undoPos_ < undoStack_.size() - 1) {
            undoPos_++;
            diagramModel_.setMemento(undoStack_.get(undoPos_));
        }
    }
    
    /**
     * saveState - save the current state of the elements_ to perform undo/redo
     *   actions in the future
     */
    public void saveLastAction() {
        currentState_ = null; // Indicate that the top of the stack no longer represents the current state
        
        // If saving a new action, blow away any following states on the stack
        while (undoStack_.size() > 0 && undoPos_ < undoStack_.size() - 1) {
            undoStack_.removeLast();
        }

        // Remove elements from front until stack is the right size
        while (undoStack_.size() > UNDO_STACK_MAX_SIZE) {
            undoStack_.removeFirst();
            undoPos_--;
        }

        undoStack_.addLast(diagramModel_.createMemento());
        undoPos_ = undoStack_.size() - 1;
    }

    public void registerSelectionObserver(SelectionObserver o) {
        selectionObservers_.add(o);
    }

    public void unregisterSelectionObserver(SelectionObserver o) {
        selectionObservers_.remove(o);
    }

    private void notifySelectionObservers() {
        for (SelectionObserver o: selectionObservers_) {
            o.notifySelectionChanged(selection_);
        }
    }
    
    public void saveFile(XMLStreamWriter writer) {
        WriteElementVisitor v = new WriteElementVisitor(writer);

        for (Element e : Lists.reverse(diagramModel_.getElements())) {
            e.accept(v);
        }
    }
    
    public void openFile(XMLStreamReader reader, int numberOfElements) {
        try {
            // Parse the XML
            for (int index = 0; index < numberOfElements; index++) {
                reader.next(); // Read element type Beginning

                // Create element at runtime
                String myElement = reader.getLocalName();
                long id = Long.parseLong(reader.getAttributeValue(null, "id"));

                if (myElement.equals("ClassElement")) {
                    ClassElement e = new ClassElement(id);
                    
                    // Read Position
                    reader.next(); // Position Beginning
                    Point p = new Point(Integer.parseInt(reader.getAttributeValue(0)),
                                        Integer.parseInt(reader.getAttributeValue(1)));
                    e.setBoxLocation(p);
                    reader.next(); // Position End

                    // Read Size
                    reader.next(); // Size Beginning
                    Dimension d = new Dimension(Integer.parseInt(reader.getAttributeValue(0)),
                                        Integer.parseInt(reader.getAttributeValue(1)));
                    e.setBoxSize(d);
                    reader.next(); // Size End
                    
                    // Read Name
                    reader.next(); // Name Beginning
                    e.setName(reader.getAttributeValue(0));
                    reader.next(); // Name End

                    // Read isAbstract
                    reader.next(); // isAbstract Beginning
                    e.setIsAbstract(reader.getAttributeValue(0).matches("true"));
                    reader.next(); // isAbstract End

                    // Read Properties
                    reader.next(); // Properties Beginning
                    while (reader.getLocalName().equals("Property")) {
                        ClassElement.Property prop = (ClassElement.Property)Class.forName(
                                reader.getAttributeValue(null, "class")).newInstance();
                        prop.name = reader.getAttributeValue(null, "name");
                        prop.type = reader.getAttributeValue(null, "type");
                        prop.visibility = ClassElement.VisibilityType.valueOf(
                                reader.getAttributeValue(null, "visibility"));
                        prop.scope = ClassElement.ScopeType.valueOf(
                                reader.getAttributeValue(null, "scope"));

                        if (prop instanceof ClassElement.Attribute)
                            e.getAttributes().add((ClassElement.Attribute)prop);
                        if (prop instanceof ClassElement.Operation)
                            e.getOperations().add((ClassElement.Operation)prop);
                        if (prop instanceof ClassElement.Parameter)
                            Iterables.getLast(e.getOperations()).parameters.add((ClassElement.Parameter)prop);

                        reader.next(); // Read end of element
                        reader.next(); // Read start of next element
                    }
                    diagramModel_.add(e);
                }
                else if (myElement.equals("Relationship")) {
                    RelationshipElement re = new RelationshipElement(id);
                    readRelationshipElement(reader, re);
                }
            }
            
            // Close the reader
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
	}
    }
    
    private void readRelationshipElement(XMLStreamReader reader, RelationshipElement re) {
        try {
            // Read Style
            reader.next(); // Style Beginning
            re.setStyle(RelationshipElement.Style.valueOf(reader.getAttributeValue(0)));
            reader.next(); // Style End

            // Read Label
            reader.next(); // Label Beginning
            re.setLabel(reader.getAttributeValue(0));
            reader.next(); // Label End

            // Read Source Class ID
            reader.next(); // Source Class ID Beginning
            re.setSource(Long.parseLong(reader.getAttributeValue(0)));
            reader.next(); // Source Class ID End

            // Read Destination Class ID
            reader.next(); // Destination Class ID Beginning
            re.setDest(Long.parseLong(reader.getAttributeValue(0)));
            reader.next(); // Destination Class ID End

            // Read SrcMultiplicity
            reader.next(); // SrcMultiplicity Beginning
            re.setSrcMultiplicity(reader.getAttributeValue(0));
            reader.next(); // SrcMultiplicity End

            // Read DestMultiplicity
            reader.next(); // DestMultiplicity Beginning
            re.setDestMultiplicity(reader.getAttributeValue(0));
            reader.next(); // DestMultiplicity End

            diagramModel_.add(re);
            
            reader.next(); // Read element type End
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
	}
    }
 
    public void deleteSelection() {
        for (Element e : selection_) {
            diagramModel_.delete(e);
        }
        clearSelection();
    }
    public void deleteDiagram() {
        diagramModel_.deleteModels();
    }
    
    public int elementCount() { return diagramModel_.elementCount(); }
}