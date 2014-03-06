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

    public AnchorElement(Element attached) {
        super(attached.getLocation());
        attached_ = attached;
        attached_.registerObserver(this);
    }

    @Override
    public void drag(Point point, int dx, int dy) {
        super.drag(point, dx, dy);
        location_.translate(dx, dy);
    }

    @Override
    public void notifyChanged(Element e) {
        location_.setLocation(Util.getClosestConnector(location_, attached_.getBounds().getBounds()));
        notifyObservers();
    }

    @Override
    public void drop(Point point) {
        super.drop(point);
        location_.setLocation(Util.getClosestConnector(location_, attached_.getBounds().getBounds()));
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}