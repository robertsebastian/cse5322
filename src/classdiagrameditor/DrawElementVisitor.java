package classdiagrameditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Collection;

public class DrawElementVisitor implements ElementVisitor {
    private final Graphics2D graphics_;
    private final DiagramManager diagram_;

    private static final Color ELEMENT_FOREGROUND_COLOR = Color.BLACK;
    private static final Color ELEMENT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR_OPAQUE = Color.RED;
    private static final Color SELECTED_COLOR = new Color(
            1.0f, 0.0f, 0.0f, 0.5f);
    private static final int BOX_PADDING = 5;

    public DrawElementVisitor(DiagramManager diagram, Graphics2D graphics2d) {
        graphics_ = graphics2d;
        diagram_ = diagram;
    }

    // Draw a series of vertically stacked boxes filled with one string per line
    private void drawStringBoxes(Rectangle bounds, Collection<String> ... boxes) {
        Rectangle drawArea = new Rectangle(bounds);
        for (Collection <String> strings : boxes) {
            if (strings.isEmpty()) continue;

            int actualHeight = drawStringBox(drawArea, strings);
            drawArea.y += actualHeight;
            drawArea.height -= actualHeight;
        }
    }

    // Draw an outlined box filled with one string per line
    private int drawStringBox(Rectangle box, Iterable<String> strings) {
        // Draw box
        graphics_.setColor(ELEMENT_BACKGROUND_COLOR);
        graphics_.fill(box);
        graphics_.setColor(ELEMENT_FOREGROUND_COLOR);
        graphics_.draw(box);

        // Narrow clipping area to just the box
        Shape lastClip = graphics_.getClip();
        graphics_.setClip(box);

        // Draw the text in the box
        int strHeight = graphics_.getFontMetrics().getHeight();
        int y = box.y + BOX_PADDING;
        for (String s : strings) {
            y += strHeight;
            graphics_.drawString(s, box.x + BOX_PADDING, y);
        }

        graphics_.setClip(lastClip);
        return y - box.y + BOX_PADDING; // Return actual drawn box height
    }

    public void visit(ClassElement e) {
        drawStringBoxes(e.getArea(), Arrays.asList(e.getName()),
                e.getProperties(), e.getOperations());

        if(diagram_.isSelected(e)) {
            Rectangle area = e.getArea();

            graphics_.setColor(SELECTED_COLOR);
            graphics_.fill(area);
            
            graphics_.setColor(SELECTED_COLOR_OPAQUE);
            graphics_.fillRect(area.x + area.width - 10, area.y + area.height - 10, 10, 10);
        }
    }

    public void visit(RelationshipElement e) {
        Point src = e.getSrcPoint();
        Point dest = e.getDestPoint();

        double x1 = src.getX();
        double y1 = src.getY();
        double x2 = dest.getX();
        double y2 = dest.getY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);
        double width = 10.0;

        // Build arrowhead polygon
        AffineTransform arrowHeadTx = new AffineTransform();
        arrowHeadTx.rotate(angle, x2, y2);
        arrowHeadTx.translate(x2, y2);
        arrowHeadTx.scale(width, width);

        double arrowHeadPts[] = new double[] {-1, 1, 0, 0, -1, -1, -0.001, 0, -1, 1};
        Polygon arrowHead = Util.buildPolygon(arrowHeadTx, arrowHeadPts);

        graphics_.setColor(diagram_.isSelected(e) ? SELECTED_COLOR_OPAQUE : Color.BLACK);

        // Draw line and arrow heads
        graphics_.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        graphics_.draw(arrowHead);

        // Draw connector points
        if (diagram_.isSelected(e)) {
            int size = 10;
            graphics_.fillOval(src.x - size / 2, src.y - size / 2, size, size);
            graphics_.fillOval(dest.x - size / 2, dest.y - size / 2, size, size);
        }
    }
}