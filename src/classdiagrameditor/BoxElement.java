package classdiagrameditor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class BoxElement extends Element {
    private static final int NUM_ANCHOR_POINTS = 16;
    private static final int MIN_DIMENSION = 5;
    private static final int RESIZE_PT_SIZE = 40;

    private boolean resizing_;
    private boolean dragging_;
    private final Rectangle area_ = new Rectangle();

    private double anchorPoints[][] = new double[2][NUM_ANCHOR_POINTS];

    public BoxElement(Point pos) {
        super();
        area_.setLocation(pos);
        area_.setSize(MIN_DIMENSION * 4, MIN_DIMENSION);
        computeAnchorPoints();
    }

    public BoxElement(BoxElement e) {
        super(e);

        area_.setBounds(e.area_);
        anchorPoints = e.anchorPoints.clone();
        anchorPoints[0] = e.anchorPoints[0].clone();
        anchorPoints[1] = e.anchorPoints[1].clone();
    }
    
    public BoxElement(long id) {
        super(id);
    }
    
    public BoxElement() {}

    public Rectangle getArea() {return area_;}
    public double[][] getAnchorPoints() {return anchorPoints;}

    public void setMinSize(Dimension size) {
        area_.setSize(Math.max(size.width, area_.width),
                Math.max(size.height, area_.height));
        computeAnchorPoints();
    }

    @Override
    public void drag(boolean multiSelect, Point start, Point end, int dx, int dy) {
        if (!resizing_ && !dragging_) {
            Rectangle resizePoint = new Rectangle(
                    area_.x + area_.width - RESIZE_PT_SIZE,
                    area_.y + area_.height - RESIZE_PT_SIZE,
                    RESIZE_PT_SIZE, RESIZE_PT_SIZE);
            if (resizePoint.contains(start)) {
                resizing_ = true;
            }
        }
        if (resizing_) {
            area_.setSize(MIN_DIMENSION, MIN_DIMENSION);
            if (end.x > area_.x && end.y > area_.y) {
               area_.add(end);
            }
        } else {
            dragging_ = true;
            area_.translate(dx, dy);
        }
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

    @Override
    public int getClosestAnchorPoint(Point p) {
        int min = 0;
        double minDist = Double.MAX_VALUE;
        for(int i = 0; i < anchorPoints[0].length; i++) {
            double dist = p.distanceSq(anchorPoints[0][i], anchorPoints[1][i]);
            if (dist < minDist) {
                min = i;
                minDist = dist;
            }
        }

        return min;
    }

    @Override
    public boolean getAnchorPoint(Point target, int i) {
        if(target.x == (int)anchorPoints[0][i] && target.y == (int)anchorPoints[1][i]) {
            // No changes to be made
            return false;
        }

        target.setLocation(anchorPoints[0][i], anchorPoints[1][i]);
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

            anchorPoints[0][i + 0 * divs] = x + scale * w;
            anchorPoints[1][i + 0 * divs] = y;
            anchorPoints[0][i + 1 * divs] = x + w;
            anchorPoints[1][i + 1 * divs] = y + scale * h;
            anchorPoints[0][i + 2 * divs] = x + offsetScale * w;
            anchorPoints[1][i + 2 * divs] = y + h;
            anchorPoints[0][i + 3 * divs] = x;
            anchorPoints[1][i + 3 * divs] = y + offsetScale * h;
        }
    }
    
    public Point getBoxLocation() {return area_.getLocation();}
    public void setBoxLocation(Point pos) {area_.setLocation(pos);}
    public Dimension getBoxSize() {return area_.getSize();}
    public void setBoxSize(Dimension size) {
        area_.setSize(size);
        computeAnchorPoints();
    }
}