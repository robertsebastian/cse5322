package classdiagrameditor;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public abstract class LineConnectorElement extends Element {

    private static final double BOUNDING_BOX_PTS[] = {0, -1, 0, 1, 1, 1, 1, -1};
    private static final double WIDTH = 20.0;

    private long srcId_;
    private long destId_;
    private Element src_ = null;
    private Element dest_ = null;

    private final Point srcPoint_ = new Point();
    private final Point destPoint_ = new Point();
    private int srcAnchor_;
    private int destAnchor_;

    private Polygon bounds_;
    private final Rectangle srcDragZone_ = new Rectangle();
    private final Rectangle destDragZone_ = new Rectangle();

    private boolean draggingSrc_;
    private boolean draggingDest_;

    public LineConnectorElement(Element src, Element dest, Point pos) {
        super();

        src_    = src;
        dest_   = dest;
        srcId_  = src_.getId();
        destId_ = dest_.getId();
        srcAnchor_ = src_.getClosestAnchorPoint(pos);
        destAnchor_ = dest_.getClosestAnchorPoint(pos);

        updateBounds();
    }

    public LineConnectorElement(LineConnectorElement e) {
        super(e);
        
        srcId_      = e.srcId_;
        destId_     = e.destId_;
        srcAnchor_  = e.srcAnchor_;
        destAnchor_ = e.destAnchor_;
    }

    public LineConnectorElement() {
        super();
    }
    public boolean isDraggingSrc() {return draggingSrc_;}
    public boolean isDraggingDest() {return draggingDest_;}

    // Update bounding box for line connecting src and dest points
    private void updateBounds() {
        // See if anything needs to be updated
        boolean srcUpdated = draggingSrc_ || getSource().getAnchorPoint(srcPoint_, srcAnchor_);
        boolean destUpdated = draggingDest_ || getDest().getAnchorPoint(destPoint_, destAnchor_);

        // Nothing to do if no updates to endpoints
        if(!srcUpdated && !destUpdated) return;

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

    public Point getSrcPoint() {
        updateBounds();
        return srcPoint_;
    }

    public Point getDestPoint() {
        updateBounds();
        return destPoint_;
    }

    public long getSourceId() {return srcId_;}
    public long getDestId() {return destId_;}

    public Element getSource() {
        if(src_ == null) src_ = model_.find(srcId_);
        return src_;
    }

    public Element getDest() {
        if(dest_ == null) dest_ = model_.find(destId_);
        return dest_;
    }

    public void setSource(long id) {
        srcId_ = id;
        src_ = null;
    }

    public void setSource(Element element) {
        srcId_ = element.getId();
        src_ = element;
    }

    public void setDest(long id) {
        srcId_ = id;
        src_ = null;
    }
    
    public void setDest(Element element) {
        destId_ = element.getId();
        dest_ = element;
    }

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
            srcAnchor_ = getSource().getClosestAnchorPoint(point);
            getSource().getAnchorPoint(srcPoint_, srcAnchor_);
        }

        if (draggingDest_) {
            destAnchor_ = getDest().getClosestAnchorPoint(point);
            getDest().getAnchorPoint(destPoint_, destAnchor_);
        }

        draggingSrc_ = false;
        draggingDest_ = false;

        updateBounds();
    }

    @Override
    public boolean contains(Point p) {
        updateBounds();
        return bounds_.contains(p) || srcDragZone_.contains(p) || destDragZone_.contains(p);
    }

    @Override
    public boolean intersects(Rectangle r) {
        updateBounds();
        return bounds_.intersects(r) || srcDragZone_.intersects(r) || destDragZone_.intersects(r);
    }
    
    @Override
    public double[][] getAnchorPoints() {
        return new double[][]{}; // Should never be called
    }

    @Override
    public int getClosestAnchorPoint(Point p) {
        return 0;
    }

    @Override
    public boolean getAnchorPoint(Point target, int i) {
        Point src = getSrcPoint();
        Point dest = getDestPoint();
        int x = (dest.x - src.x) / 2 + src.x;
        int y = (dest.y - src.y) / 2 + src.y;

        // Indicate not updated
        if (target.x == x && target.y == y) return false;

        // Indicate updated
        target.x = x;
        target.y = y;
        return true;
    }
}