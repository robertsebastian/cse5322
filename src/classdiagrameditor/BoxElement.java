package classdiagrameditor;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class BoxElement extends Element {
    private static final int NUM_ANCHOR_POINTS = 16;
    private static final int MIN_DIMENSION = 40;

    private boolean resizing_;
    private boolean dragging_;
    private final Rectangle area_ = new Rectangle();

    private double[][] anchorPoints_ = new double[2][NUM_ANCHOR_POINTS];

    public BoxElement(Point pos) {
        super();
        area_.setLocation(pos);
        area_.setSize(MIN_DIMENSION * 4, MIN_DIMENSION * 2);
        computeAnchorPoints();
    }

    public BoxElement(BoxElement e) {
        super(e);

        area_.setBounds(e.area_);
        anchorPoints_ = e.anchorPoints_.clone();
        anchorPoints_[0] = e.anchorPoints_[0].clone();
        anchorPoints_[1] = e.anchorPoints_[1].clone();
    }

    public Rectangle getArea() {return area_;}
    public double[][] getAnchorPoints() {return anchorPoints_;}

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
            area_.translate(dx, dy); }
        computeAnchorPoints();
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
        for(int i = 0; i < anchorPoints_[0].length; i++) {
            double dist = p.distanceSq(anchorPoints_[0][i], anchorPoints_[1][i]);
            if (dist < minDist) {
                min = i;
                minDist = dist;
            }
        }

        return min;
    }

    public boolean getAnchorPoint(Point target, int i) {
        if(target.x == (int)anchorPoints_[0][i] && target.y == (int)anchorPoints_[1][i]) {
            // No changes to be made
            return false;
        }

        target.setLocation(anchorPoints_[0][i], anchorPoints_[1][i]);
        return true;
    }

    private void computeAnchorPoints() {
        int divs = NUM_ANCHOR_POINTS / 4;
        double x = area_.getX();
        double y = area_.getY();
        double w = area_.getWidth();
        double h = area_.getHeight();
        for(int i = 0; i < NUM_ANCHOR_POINTS / 4; i++) {
            double scale = (double)i / (double)divs;
            double offsetScale = (double)(i + 1) / (double)divs;

            anchorPoints_[0][i + 0 * divs] = x + scale * w;
            anchorPoints_[1][i + 0 * divs] = y;
            anchorPoints_[0][i + 1 * divs] = x + w;
            anchorPoints_[1][i + 1 * divs] = y + scale * h;
            anchorPoints_[0][i + 2 * divs] = x + offsetScale * w;
            anchorPoints_[1][i + 2 * divs] = y + h;
            anchorPoints_[0][i + 3 * divs] = x;
            anchorPoints_[1][i + 3 * divs] = y + offsetScale * h;
        }
    }
}