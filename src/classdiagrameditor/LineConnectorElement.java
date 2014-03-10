package classdiagrameditor;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class LineConnectorElement extends Element {
    private static final double BOUNDING_BOX_PTS[] = {0, -1, 0, 1, 1, 1, 1, -1};
    private static final double WIDTH = 20.0;

    private final BoxElement src_;
    private final BoxElement dest_;

    private final Point srcPoint_ = new Point();
    private final Point destPoint_ = new Point();
    private int srcAnchor_;
    private int destAnchor_;

    private Polygon bounds_;
    private final Rectangle srcDragZone_ = new Rectangle();
    private final Rectangle destDragZone_ = new Rectangle();

    private boolean draggingSrc_;
    private boolean draggingDest_;

    public LineConnectorElement(BoxElement src, BoxElement dest) {
        super();

        src_  = src;
        dest_ = dest;

        // Listen for changes in position of src or dest
        src_.registerObserver(this);
        dest_.registerObserver(this);

        // Initialize with the closest points to the center of the src and dest
        Rectangle box = new Rectangle();
        box.add(src_.getArea());
        box.add(dest_.getArea());
        Point center = new Point((int)box.getCenterX(), (int)box.getCenterY());

        srcAnchor_ = src_.getClosestAnchorPoint(center);
        src_.getAnchorPoint(srcPoint_, srcAnchor_);
        destAnchor_ = dest_.getClosestAnchorPoint(center);
        dest_.getAnchorPoint(destPoint_, destAnchor_);
        
        updateBounds();
    }

    // Update bounding box for line connecting src and dest points
    private void updateBounds() {
        double x1 = srcPoint_.getX();
        double y1 = srcPoint_.getY();
        double x2 = destPoint_.getX();
        double y2 = destPoint_.getY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len   = Math.hypot(x2 - x1, y2 - y1);

        // Build bounding box
        AffineTransform tx = new AffineTransform();
        tx.rotate(angle, x1, y1);
        tx.translate(x1, y1);
        tx.scale(len, WIDTH);

        bounds_ = Util.buildPolygon(tx, BOUNDING_BOX_PTS);
        srcDragZone_.setLocation(srcPoint_);
        srcDragZone_.setSize(0, 0);
        srcDragZone_.grow((int)WIDTH, (int)WIDTH);

        destDragZone_.setLocation(destPoint_);
        destDragZone_.setSize(0, 0);
        destDragZone_.grow((int)WIDTH, (int)WIDTH);
    }

    public Point getSrcPoint() {return srcPoint_;}
    public Point getDestPoint() {return destPoint_;}
    public Element getSource() {return src_;}
    public Element getDest() {return dest_;}

    @Override
    public void drag(boolean multiSelect, Point start, Point end, int dx, int dy) {
        if (multiSelect) return; // Can only drag endpoints, so nothing to do if
                                 // dragging multiple elements

        // Start dragging a control point 
        if (!draggingSrc_ && !draggingDest_) { 
            draggingSrc_ = start.distance(srcPoint_) <= WIDTH;
        } 

        if (!draggingSrc_ && !draggingDest_) {
            draggingDest_ = start.distance(destPoint_) <= WIDTH;
        }

        if (draggingSrc_) srcPoint_.translate(dx, dy);
        if (draggingDest_) destPoint_.translate(dx, dy);

        updateBounds();
    }

    @Override
    public void drop(Point point) {
        if (draggingSrc_) {
            srcAnchor_ = src_.getClosestAnchorPoint(point);
            src_.getAnchorPoint(srcPoint_, srcAnchor_);
        }

        if (draggingDest_) {
            destAnchor_ = dest_.getClosestAnchorPoint(point);
            dest_.getAnchorPoint(destPoint_, destAnchor_);
        }

        draggingSrc_ = false;
        draggingDest_ = false;

        updateBounds();
    }

    @Override
    public boolean contains(Point p) {
        return bounds_.contains(p) || srcDragZone_.contains(p) || destDragZone_.contains(p);
    }

    @Override
    public boolean intersects(Rectangle r) {
        return bounds_.intersects(r) || srcDragZone_.intersects(r) || destDragZone_.intersects(r);
    }

    @Override
    public void notifyElementChanged(Element e) {
        src_.getAnchorPoint(srcPoint_, srcAnchor_);
        dest_.getAnchorPoint(destPoint_, destAnchor_);
        updateBounds();
    }
}