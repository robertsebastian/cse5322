package classdiagrameditor;

import java.awt.Graphics2D;
import java.awt.Shape;

public abstract class DiagramElement
    implements Comparable {

    private final long id_; // Unique identifier

    public DiagramElement(long id) {
        id_ = id;
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(id_, ((DiagramElement)o).id_);
    }

    public long getId() {return id_;}
    public abstract Shape getShape();
    public abstract void translate(int dx, int dy);
    public abstract void draw(Graphics2D graphics, boolean isSelected);
}