package classdiagrameditor;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DiagramElement
    implements Comparable {

    protected static final int PADDING = 5;

    private long id_;
    private final Rectangle bounds_; // X, Y, with, and height of this element

    @Override
    public int compareTo(Object o) {
        return Long.compare(getId(), ((DiagramElement)o).getId());
    }

    public DiagramElement() {
        bounds_ = new Rectangle();
        id_ = -1;
    }

    public Rectangle getBounds() {
        return bounds_;
    }

    public long getId() {return id_;}
    public void setId(long id) {id_ = id;}

    /**
     * Draw this element into the given graphics context
     * @param graphics graphics context provided by a JComponent's paint method
     */
    public void draw(Graphics2D graphics) {
        // For this base class, just draw a generic bounding box
        graphics.draw(bounds_);
    }
}