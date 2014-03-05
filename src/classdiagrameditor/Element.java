package classdiagrameditor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public abstract class Element
    implements Comparable {

    protected Point location_;

    private final long id_; // Unique identifier
    private Shape bounds_;  // Bounding box for determining selection
    private boolean isDragging_ = false;

    public Element(long id) {
        this(id, new Point());
    }

    public Element(long id, Point pos) {
        id_       = id;
        location_ = new Point(pos);
        bounds_   = new Rectangle(pos);
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(id_, ((Element)o).id_);
    }

    public long getId() {return id_;}

    public Point getLocation() {return location_;}

    public Shape getBounds() {return bounds_;}
    public void setBounds(Shape shape) {bounds_ = shape;}
    public boolean isDragging() {return isDragging_;}

    public void drag(Point point, int dx, int dy) {
        location_.translate(dx, dy);
        isDragging_ = true;
    }

    public void drop(Point point) {
        isDragging_ = false;
    }

    public void draw(Graphics2D graphics, boolean isSelected) {
        graphics.draw(bounds_);
    }
}