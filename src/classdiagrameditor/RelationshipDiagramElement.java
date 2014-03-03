package classdiagrameditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

public class RelationshipDiagramElement extends DiagramElement {
    private final DiagramElement src_;
    private final DiagramElement dest_;
    private final Polygon bounds_ = new Polygon();

    private final int selectionPadding = 15;

    public RelationshipDiagramElement(long id, DiagramElement src, DiagramElement dest) {
        super(id);
        src_ = src;
        dest_ = dest;
    }

    @Override
    public Shape getShape() {
        return bounds_;
    }

    // Drawn in terms of src and dest elements, so nothing to do here
    @Override
    public void translate(int dx, int dy) {}

    @Override
    public void draw(Graphics2D graphics, boolean isSelected) {
        Rectangle b1 = src_.getShape().getBounds();
        Rectangle b2 = dest_.getShape().getBounds();

        int x1 = (int)b1.getCenterX();
        int x2 = (int)b2.getCenterX();
        int y1 = (int)b1.getCenterY();
        int y2 = (int)b2.getCenterY();

        graphics.setColor(isSelected ? EditorPanel.SELECTED_COLOR : Color.BLACK);
        graphics.drawLine(x1, y1, x2, y2);

        bounds_.reset();
        bounds_.addPoint(x1, y1 - selectionPadding);
        bounds_.addPoint(x1, y1 + selectionPadding);
        bounds_.addPoint(x2, y2 + selectionPadding);
        bounds_.addPoint(x2, y2 - selectionPadding);
    }
}