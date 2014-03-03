package classdiagrameditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class EditorPanel extends JPanel
    implements MouseInputListener {

    // Styles to be used within diagram draw routines
    public static final Stroke SOLID_STROKE = new BasicStroke();
    public static final Stroke DASHED_STROKE = new BasicStroke(
            1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10.0f, new float [] {1.0f}, 0.0f);

    // Model of current diagram state
    private DiagramModel diagramModel;

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

    // Edit state
    private enum EditState {
        EDIT,       // Default mode - Clicking selects an element
        ADD_SINGLE, // Adding element with single point
        ADD_DOUBLE, // Adding element with two endpoints
    };
    private EditState editState_ = EditState.ADD_SINGLE;

    public EditorPanel() {
        super();

        setOpaque(true);

        diagramModel = new DiagramModel();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(Color.WHITE);

        Graphics2D g2 = (Graphics2D)g; // Only accept 2D graphics context

        super.paintComponent(g2);

        diagramModel.draw(g2);

        if(dragState_ == DragState.SELECTION_BOX) {
            g2.setColor(Color.BLACK);
            g2.setStroke(DASHED_STROKE);
            g2.draw(dragRect_);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPos = new Point(e.getX(), e.getY());

        // FIXME: No add buttons, so force into add state on shift down
        if(e.isShiftDown()) {
            editState_ = EditState.ADD_SINGLE;
        } else {
            editState_ = EditState.EDIT;
        }

        // Decide what to do with click based on state
        switch (editState_) {
        case EDIT:
            if(e.isControlDown()) {
                // Toggle selection if ctrl is down
                diagramModel.addSelection(clickPos, true);
            } else {
                // Otherwise reset selection to clicked item
                diagramModel.clearSelection();
                diagramModel.addSelection(clickPos, false);
            }
            break;

        case ADD_SINGLE:
            diagramModel.createClass(clickPos);
            break;
        }

        // Redraw this component
        repaint(getBounds());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart_.setLocation(e.getX(), e.getY());
        dragEnd_.setLocation(e.getX(), e.getY());
        dragRect_.setBounds(0, 0, 0, 0);
        dragState_ = diagramModel.isPointInSelection(dragStart_) ?
                DragState.RELOCATE : DragState.SELECTION_BOX;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragRect_.setBounds(0, 0, 0, 0);
        dragState_ = DragState.NONE;
        repaint(getBounds());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - dragEnd_.x;
        int dy = e.getY() - dragEnd_.y;

        // Rebuild bounding box with start and end points
        dragEnd_.setLocation(e.getX(), e.getY());
        dragRect_.setBounds(0, 0, -1, -1);
        dragRect_.add(dragStart_);
        dragRect_.add(dragEnd_);

        // Update model with new drag info
        if(dragState_ == DragState.RELOCATE) {
            // Move selected objects around
            diagramModel.moveSelection(dx, dy);
        } else if(dragState_ == DragState.SELECTION_BOX) {
            // Change selection box size
            diagramModel.clearSelection();
            diagramModel.addSelection(dragRect_);
        }
        repaint(getBounds());
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}