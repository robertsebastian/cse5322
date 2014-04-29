package classdiagrameditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.event.MouseInputListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class EditorPanel extends JPanel
    implements MouseInputListener, Observer {

    // Styles to be used within diagram draw routines
    public static final Stroke SOLID_STROKE = new BasicStroke();
    public static final Stroke DASHED_STROKE = new BasicStroke(
            1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10.0f, new float [] {3.0f}, 0.0f);
    public static final Color SELECTED_COLOR = new Color(
            1.0f, 0.0f, 0.0f, 0.5f);
    public static final Color HELPER_TEXT_BOX_COLOR = new Color(
            1.0f, 0.8f, 0.5f, 1.0f);

    private final ClassMenuPopUp contextMenu_ = new ClassMenuPopUp();

    // Model of current diagram state
    private final DiagramManager diagram_ = new DiagramManager();

    // State of mouse dragging action
    private enum DragState {
        NONE,          // Not dragging
        SELECTION_BOX, // Selecting a group of elements
        RELOCATE       // Relocating selected elements
    };
    private DragState dragState_ = DragState.NONE;
    private final Point dragStart_ = new Point();
    private final Point dragEnd_ = new Point();
    private final Rectangle dragRect_ = new Rectangle();
    private boolean firstDragEvent_;

    // Edit state
    private EditState editState_ = new EditingState();

    // Helper text shown in top-right corner
    private String helperText_ = "";

    private String diagramName_ = "";
    private String packageName_ = "";

    public EditorPanel() {
        super();

        setOpaque(true);
        setBackground(Color.WHITE);

        addMouseListener(this);
        addMouseMotionListener(this);

        diagram_.addObserver(this);
    }

    public DiagramManager getManager() {
        return diagram_;
    }

    public void close() {
        diagram_.close();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g; // Only accept 2D graphics context
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw whole diagram
        diagram_.draw(g2);

        // Draw drag selection box
        if(dragState_ == DragState.SELECTION_BOX) {
            g2.setColor(Color.BLACK);
            g2.setStroke(DASHED_STROKE);
            g2.draw(dragRect_);
            g2.setStroke(SOLID_STROKE);
        }

        // Draw helper text
        if(!helperText_.isEmpty()) {
            Rectangle bounds = g2.getFontMetrics().getStringBounds(helperText_, g2).getBounds();
            bounds.grow(5, 5);
            bounds.setLocation(g2.getClipBounds().width - bounds.width - 5, 5);

            g2.setColor(HELPER_TEXT_BOX_COLOR);
            g2.fill(bounds);
            g2.setColor(Color.BLACK);
            g2.draw(bounds);
            g2.drawString(helperText_, bounds.x + 5, bounds.y + bounds.height - 5);
        }
    }

    /**
     * Open the context menu pop-up if the mouse event is the correct trigger
     * @param e        Mouse event (should be a press or release)
     * @return True if the event was handled here
     */
    private boolean handleContextMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            contextMenu_.show(e.getComponent(), e.getX(), e.getY());
            return true;
        }
        return false;
    }

    private abstract class EditState {
        public EditState() {
            setHelperText("");
        }

        public EditState mouseClicked(MouseEvent e, Point clickPos) {
            return this;
        }
    }

    private class EditingState extends EditState {

        public EditingState() {
            setHelperText("");
        }

        public EditState mouseClicked(MouseEvent e, Point clickPos) {
            if(e.isControlDown()) {
                // Toggle selection if ctrl is down
                diagram_.addSelection(clickPos, true);
            } else {
                // Otherwise reset selection to clicked item
                if (!SwingUtilities.isRightMouseButton(e))
                {
                    diagram_.clearSelection();
                }
                diagram_.addSelection(clickPos, false);
            }

            return this;
        }
    }

    private class AddingClassState extends EditState {
        public AddingClassState() {
            setHelperText("Click location for new class");
        }

        public EditState mouseClicked(MouseEvent e, Point clickPos) {
            diagram_.createClass(clickPos, packageName_);
            return new EditingState();
        }
    }

    private class AddingRelationshipState extends EditState {
        private Element firstElement_;
        private Class<?> type_;

        public AddingRelationshipState(Class<?> type) {
            type_ = type;
            setHelperText("Click source class");
        }

        public EditState mouseClicked(MouseEvent e, Point clickPos) {
            Element clickedElement = diagram_.findClassElementByPos(clickPos);
            // Nothing to do if nothing valid clicked
            if (clickedElement == null) return this;

            // Get first element if it hasn't already been set
            if(firstElement_ == null || !(firstElement_ instanceof ClassElement)) {
                firstElement_ = clickedElement;
                setHelperText("Click destination element");
                return this;
            }

            // Add relationship
            if(clickedElement != firstElement_) {
                diagram_.createRelationship(type_, firstElement_, clickedElement, clickPos);
                return new EditingState();
            }

            return this;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPos = new Point(e.getX(), e.getY());

        editState_ = editState_.mouseClicked(e, clickPos);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point pos = new Point(e.getX(), e.getY());
        
        if (handleContextMenu(e)) return;

        // Allow movement by dragging on an unselected element
        if(editState_ instanceof EditingState && !diagram_.isPointInSelection(pos) && !e.isControlDown()) {
            diagram_.clearSelection();
            diagram_.addSelection(pos, false);
        }

        // Save off dragging state
        firstDragEvent_ = true;
        dragStart_.setLocation(pos);
        dragEnd_.setLocation(pos);
        dragRect_.setBounds(0, 0, 0, 0);

        // If dragging on a selected item, move it, otherwise start a selection box
        dragState_ = diagram_.isPointInSelection(pos) ?
                DragState.RELOCATE : DragState.SELECTION_BOX;

        repaint(getBounds());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point pos = new Point(e.getX(), e.getY());

        handleContextMenu(e);

        // Drop the selection on mouse release
        if(dragState_ == DragState.RELOCATE) {
            diagram_.dropSelection(pos);
        }

        // Clear dragging state
        firstDragEvent_ = false;
        dragRect_.setBounds(0, 0, 0, 0);
        dragState_ = DragState.NONE;

        repaint(getBounds());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point pos = new Point(e.getX(), e.getY());

        int dx = pos.x - dragEnd_.x;
        int dy = pos.y - dragEnd_.y;

        // Update dragging state
        dragEnd_.setLocation(pos);
        dragRect_.setBounds(0, 0, -1, -1);
        dragRect_.add(dragStart_);
        dragRect_.add(dragEnd_);
        
        // Update model with new drag info
        if(dragState_ == DragState.RELOCATE) {
            // Move selected objects around
            diagram_.dragSelection(firstDragEvent_, dragStart_, pos, dx, dy);
        } else if(dragState_ == DragState.SELECTION_BOX) {
            // Change selection box size
            if(!e.isControlDown()) diagram_.clearSelection();
            diagram_.addSelection(dragRect_);
        }

        firstDragEvent_ = false;

        repaint(getBounds());
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    public void addClass() {
        editState_ = new AddingClassState();
    }

    public void addRelationship() {
        // TODO: No generic add
    }
    
    public void addDependency() {
        editState_ = new AddingRelationshipState(DependencyRelationship.class);
    }
    
    public void addAssociation() {
        editState_ = new AddingRelationshipState(AssociationRelationship.class);
    }
    
    public void addComposition() {
        editState_ = new AddingRelationshipState(CompositionRelationship.class);
    }
    
    public void addAggregation() {
        editState_ = new AddingRelationshipState(AggregationRelationship.class);
    }
    
    public void addInheritance() {
        editState_ = new AddingRelationshipState(InheritanceRelationship.class);
    }

    /**
     * undoLastAction - Set the editState to EDIT, output to the user helper text,
     * and call upon diagram to undo the last action saved by the memento class
     */
    public void undoLastAction() {
        editState_ = new EditingState();
        setHelperText("Last action undone.");
        diagram_.undoLastAction();
    }
    
    /**
     * redoLastAction - Set the editState to EDIT, output to the user helper text,
     * and call upon diagram to redo the last action saved by the memento class
     */
    public void redoLastAction() {
        editState_ = new EditingState();
        setHelperText("last action redone.");
        diagram_.redoLastAction();
    }
    
    public void saveFile(XMLStreamWriter writer) {
        diagram_.saveFile(writer);
    }
    
    public void openFile(XMLStreamReader reader, int numberOfElements) throws XMLStreamException {
        diagram_.openFile(reader, numberOfElements);
    }
    
    public void deleteSelection() {
        int button = JOptionPane.YES_NO_OPTION;
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected element(s)?", "Warning", button);

        if(response == JOptionPane.YES_OPTION)
        {
            diagram_.deleteSelection();
        }                               
    }

    class PopupActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if ("Cut".equals(ae.getActionCommand())) {
                getManager().cut();
            } else if ("Copy".equals(ae.getActionCommand())) {
                getManager().copy();
            } else if ("Paste".equals(ae.getActionCommand())) {
                getManager().paste();
            } else if ("Delete".equals(ae.getActionCommand())) {
                deleteSelection();
            }
        }
    }

    class ClassMenuPopUp extends JPopupMenu {
        ActionListener actionListener = new PopupActionListener();
        public ClassMenuPopUp() {
            JMenuItem anItem = new JMenuItem("Cut");
            add(anItem);
            anItem.addActionListener(actionListener);
            anItem = new JMenuItem("Copy");
            add(anItem);
            anItem.addActionListener(actionListener);
            anItem = new JMenuItem("Paste");
            add(anItem);
            anItem.addActionListener(actionListener);
            anItem = new JMenuItem("Delete");
            add(anItem);
            anItem.addActionListener(actionListener);
        }
    }

    // Called when diagram state has been updated
    public void update(Observable o, Object arg) {
        repaint(getBounds());
    }

    public void setHelperText(String text) {
        helperText_ = text;
        repaint(getBounds());
    }
    
    public void setPackageName(String pkg) {
        packageName_ = pkg;
    }

    public String getPackageName() {
        return packageName_;
    }
    
    public void setDiagramName(String diagram) {
        diagramName_ = diagram;
    }
        
    public String getDiagramName() {
        return diagramName_;
    }   
}