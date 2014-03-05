package classdiagrameditor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author alex
 */
public class AnchorElement extends Element {
    Element attached_;

    public AnchorElement(long id, Element attached) {
        super(id, attached.getLocation());
        attached_ = attached;
    }

    @Override
    public void draw(Graphics2D graphics, boolean isSelected) {
        int width = 20;

        if(!isDragging()) {
            location_.setLocation(Util.getClosestConnector(location_, attached_.getBounds().getBounds()));
        }

        Rectangle newBounds = new Rectangle(
                location_.x - width / 2, location_.y - width / 2,
                width, width);
        setBounds(newBounds);

        if(isSelected) {
            graphics.draw(newBounds);
        }
    }
}
