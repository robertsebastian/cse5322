package classdiagrameditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class EditorPanel extends JPanel
    implements MouseInputListener {

    // Styles to be used within diagram draw routines
    public static final Stroke SOLID_STROKE = new BasicStroke();
    public static final Stroke DASHED_STROKE = new BasicStroke(
            1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10.0f, new float [] {3.0f}, 0.0f);
    public static final Color SELECTED_COLOR = new Color(
            1.0f, 0.0f, 0.0f, 0.5f);

    // Model of current diagram state
    private DiagramController diagram_;

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
    private Point lastClickPos_;

    // Helper text shown in top-right corner
    private String helperText = ""
            + "Shift-Click: Add class\n"
            + "Shift+Ctrl+Click: Add relationship\n";

    public EditorPanel() {
        super();

        setOpaque(true);

        diagram_ = new DiagramController();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(Color.WHITE);

        Graphics2D g2 = (Graphics2D)g; // Only accept 2D graphics context
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);

        // Draw whole diagram
        diagram_.draw(g2);

        // Draw drag selection box
        if(dragState_ == DragState.SELECTION_BOX) {
            g2.setColor(Color.BLACK);
            g2.setStroke(DASHED_STROKE);
            g2.draw(dragRect_);
        }

        // Draw helper text
        if(!helperText.isEmpty()) {
            g2.setColor(Color.BLACK);
            Rectangle2D bounds = g2.getFontMetrics().getStringBounds(helperText, g2);
            g2.drawString(helperText, 0, (int)bounds.getHeight());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPos = new Point(e.getX(), e.getY());

        // Decide what to do with click based on state
        switch (editState_) {
        case EDIT:
            if(e.isControlDown()) {
                // Toggle selection if ctrl is down
                diagram_.addSelection(clickPos, true);
            } else {
                // Otherwise reset selection to clicked item
                diagram_.clearSelection();
                diagram_.addSelection(clickPos, false);
            }
            break;

        case ADD_SINGLE:
            diagram_.createClass(clickPos);
            break;

        case ADD_DOUBLE:
            if(lastClickPos_ == null) {
                lastClickPos_ = clickPos;
            } else {
                diagram_.createRelationship(lastClickPos_, clickPos);
                lastClickPos_ = null;
            }
            break;
        }

        // Redraw this component
        repaint(getBounds());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point pos = new Point(e.getX(), e.getY());

        // FIXME: No add buttons, so force edit states based on shift/ctrl state
        if(e.isShiftDown() && e.isControlDown()) {
            editState_ = EditState.ADD_DOUBLE;
        } else if(e.isShiftDown()) {
            editState_ = EditState.ADD_SINGLE;
        } else {
            editState_ = EditState.EDIT;
        }

        // Allow movement by dragging on an unselected element
        if(editState_ == EditState.EDIT && !diagram_.isPointInSelection(pos) && !e.isControlDown()) {
            diagram_.clearSelection();
            diagram_.addSelection(pos, false);
        }

        dragStart_.setLocation(pos);
        dragEnd_.setLocation(pos);
        dragRect_.setBounds(0, 0, 0, 0);
        dragState_ = diagram_.isPointInSelection(pos) ?
                DragState.RELOCATE : DragState.SELECTION_BOX;

        repaint(getBounds());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(dragState_ == DragState.RELOCATE) {
            diagram_.dropSelection(new Point(e.getX(), e.getY()));
        }

        dragRect_.setBounds(0, 0, 0, 0);
        dragState_ = DragState.NONE;

        repaint(getBounds());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point pos = new Point(e.getX(), e.getY());

        int dx = pos.x - dragEnd_.x;
        int dy = pos.y - dragEnd_.y;

        // Rebuild bounding box with start and end points
        dragEnd_.setLocation(pos);
        dragRect_.setBounds(0, 0, -1, -1);
        dragRect_.add(dragStart_);
        dragRect_.add(dragEnd_);
        
        // Update model with new drag info
        if(dragState_ == DragState.RELOCATE) {
            // Move selected objects around
            diagram_.dragSelection(dragStart_, pos, dx, dy);
        } else if(dragState_ == DragState.SELECTION_BOX) {
            // Change selection box size
            if(!e.isControlDown()) diagram_.clearSelection();
            diagram_.addSelection(dragRect_);
        }

        repaint(getBounds());
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}