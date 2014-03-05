package classdiagrameditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class RelationshipElement extends Element {
    private final Element src_;
    private final Element dest_;

    private final int selectionPadding = 15;

    public RelationshipElement(long id, Element src, Element dest) {
        super(id);
        src_ = src;
        dest_ = dest;
    }

    // Drawn in terms of src and dest elements, so nothing to do here
    @Override
    public void drag(Point point, int dx, int dy) {}

    @Override
    public void drop(Point point) {}

    @Override
    public void draw(Graphics2D graphics, boolean isSelected) {
        Rectangle b1 = src_.getBounds().getBounds();
        Rectangle b2 = dest_.getBounds().getBounds();

        //double conn[] = Util.getConnectingLine(b1, b2);
        //double x1 = conn[0];
        //double y1 = conn[1];
        //double x2 = conn[2];
        //double y2 = conn[3];

        double x1 = b1.getCenterX();
        double x2 = b2.getCenterX();
        double y1 = b1.getCenterY();
        double y2 = b2.getCenterY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;

        // Build bounding box
        AffineTransform tx = new AffineTransform();
        tx.rotate(angle, x1, y1);
        tx.translate(x1, y1);
        tx.scale(len, width);

        double boundingBoxPts[] = new double[] {0, -1, 0, 1, 1, 1, 1, -1};
        Polygon bounds = Util.buildPolygon(tx, boundingBoxPts);
        setBounds(bounds);

        // Build arrowhead polygon
        AffineTransform arrowHeadTx = new AffineTransform();
        arrowHeadTx.rotate(angle, x2, y2);
        arrowHeadTx.translate(x2, y2);
        arrowHeadTx.scale(width, width);

        double arrowHeadPts[] = new double[] {-1, 1, 0, 0, -1, -1, -0.001, 0, -1, 1};
        Polygon arrowHead = Util.buildPolygon(arrowHeadTx, arrowHeadPts);

        graphics.setColor(isSelected ? EditorPanel.SELECTED_COLOR : Color.BLACK);
        graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        graphics.draw(arrowHead);
    }
}