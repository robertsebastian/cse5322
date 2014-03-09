package classdiagrameditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.PathIterator;

public abstract class BoxElement extends Element {
    private static final int NUM_ANCHOR_POINTS = 16;
    private static final int MIN_DIMENSION = 40;
    private boolean resizing_;
    private boolean dragging_;
    private final Rectangle area_ = new Rectangle();

    private final double anchorPointsX[] = new double[NUM_ANCHOR_POINTS];
    private final double anchorPointsY[] = new double[NUM_ANCHOR_POINTS];

    public BoxElement(Point pos) {
        super();
        area_.setLocation(pos);
        area_.setSize(MIN_DIMENSION * 4, MIN_DIMENSION * 2);
        computeAnchorPoints();
    }

    public Rectangle getArea() {return area_;}

    @Override
    public void drag(boolean multiSelect, Point start, Point end, int dx, int dy) {
        if (!resizing_ && !dragging_) {
            Rectangle resizePoint = new Rectangle(area_.x + area_.width - MIN_DIMENSION, area_.y + area_.height - MIN_DIMENSION, MIN_DIMENSION, MIN_DIMENSION);
            if (resizePoint.contains(start)) {
                resizing_ = true;
            }
        }
        if (resizing_) {
            area_.setSize(MIN_DIMENSION, MIN_DIMENSION);
            area_.add(end);
        } else {
            dragging_ = true;
            area_.translate(dx, dy);
        }
        computeAnchorPoints();
        notifyObservers();
    }

    @Override
    public void drop(Point pos) {
        resizing_ = false;
        dragging_ = false;
    }

    @Override
    public boolean contains(Point point) {
        return area_.contains(point);
    }

    @Override
    public boolean intersects(Rectangle rectangle) {
        return area_.intersects(rectangle);
    }

    public int getClosestAnchorPoint(Point p) {
        int min = 0;
        double minDist = Double.MAX_VALUE;
        for(int i = 0; i < anchorPointsX.length; i++) {
            double dist = p.distanceSq(anchorPointsX[i], anchorPointsY[i]);
            if (dist < minDist) {
                min = i;
                minDist = dist;
            }
        }

        return min;
    }

    public void getAnchorPoint(Point target, int i) {
        target.setLocation(anchorPointsX[i], anchorPointsY[i]);
    }

    private void computeAnchorPoints() {
        // 1  x + 0     y + 0
        // 2  x + w/2   y + 0

        // 3  x + w     y + 0
        // 4  x + w     y + h/2

        // 6  x + w/2   y + h
        // 5  x + w     y + h

        // 8  x + 0     y + h/2
        // 7  x + 0     y + h
        int divs = NUM_ANCHOR_POINTS / 4;
        double x = area_.getX();
        double y = area_.getY();
        double w = area_.getWidth();
        double h = area_.getHeight();
        for(int i = 0; i < NUM_ANCHOR_POINTS / 4; i++) {
            double scale = (double)i / (double)divs;
            double offsetScale = (double)(i + 1) / (double)divs;

            anchorPointsX[i + 0 * divs] = x + scale * w;
            anchorPointsY[i + 0 * divs] = y;
            anchorPointsX[i + 1 * divs] = x + w;
            anchorPointsY[i + 1 * divs] = y + scale * h;
            anchorPointsX[i + 2 * divs] = x + offsetScale * w;
            anchorPointsY[i + 2 * divs] = y + h;
            anchorPointsX[i + 3 * divs] = x;
            anchorPointsY[i + 3 * divs] = y + offsetScale * h;
        }
    }
}