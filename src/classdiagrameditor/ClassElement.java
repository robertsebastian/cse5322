package classdiagrameditor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author alex
 */
public class ClassElement extends Element {
    public static final int boxPadding = 10;

    private String name_;             // Name of class
    private boolean isAbstract_;      // True if this represents an abstract class
    private List<String> properties_; // List of class properties
    private List<String> operations_; // List of class operations

    public ClassElement(long id, Point pos) {
        super(id, pos);

        name_ = "NewClass" + id;
        isAbstract_ = false;
        properties_ = new LinkedList<String>();
        operations_ = new LinkedList<String>();

        properties_.add("attribute1 : String");
        properties_.add("attribute2 : String");
        properties_.add("attribute3 : String");
        properties_.add("attribute4 : String");

        operations_.add("operation1(String x, int y)");
        operations_.add("operation2(double x, double y, double z)");
        operations_.add("operation3(List x, int y)");
    }

    // Draw a series of vertically stacked boxes filled with one string per line
    private static Rectangle drawStringBoxes(Graphics2D graphics, Point pos, Collection<String> ... boxes) {
        // Calculate width and height of longest string using font metrics
        FontMetrics fm = graphics.getFontMetrics();

        int maxHeight = fm.getHeight();
        int maxWidth = 0;
        for (Collection <String> strings : boxes) {
            for (String s : strings) {
                maxWidth = Math.max(fm.stringWidth(s), maxWidth);
            }
        }

        // Draw boxes
        Rectangle bounds = new Rectangle(pos);
        Rectangle box = new Rectangle(pos);
        for (Collection <String> strings : boxes) {
            if (strings.isEmpty()) continue;
            
            // Draw box
            box.width = maxWidth + boxPadding * 2;
            box.height = boxPadding * 2 + strings.size() * maxHeight;
            drawStringBox(graphics, box, strings);

            // Shift down to next box
            bounds.add(box);
            box.y += box.height;
        }

        return bounds;
    }

    // Draw an outlined box filled with one string per line
    private static void drawStringBox(Graphics2D graphics, Rectangle box, Iterable<String> strings) {
        graphics.setColor(Color.WHITE);
        graphics.fill(box);
        graphics.setColor(Color.BLACK);
        graphics.draw(box);

        int strHeight = graphics.getFontMetrics().getHeight();
        int x = box.x + boxPadding;
        int y = box.y + boxPadding + strHeight;

        for (String s : strings) {
            graphics.drawString(s, x, y);
            y += strHeight;
        }
    }

    @Override
    public void draw(Graphics2D graphics, boolean isSelected) {
        Rectangle newBounds =  drawStringBoxes(graphics, location_,
                Arrays.asList(name_), properties_, operations_);
        setBounds(newBounds);

        if(isSelected) {
            graphics.setColor(EditorPanel.SELECTED_COLOR);
            graphics.fill(newBounds);
        }
    }
}