package classdiagrameditor;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Util {
    /**
     * Transform an array of points {x1, y1, x2, y2, ...} into a shape
     * @param tx AffineTransform transformation
     * @param pts Array of points
     * @return Polygon built using pts after applying tx
     */
    public static Polygon buildPolygon(AffineTransform tx, double pts[]) {
        double txPts[] = new double[pts.length];
        tx.transform(pts, 0, txPts, 0, pts.length / 2);

        Polygon p = new Polygon();
        for (int i = 0; i < txPts.length; i += 2) {
            p.addPoint((int)txPts[i], (int)txPts[i + 1]);
        }
        return p;
    }

    public static Point getClosestConnector(Point pos, Rectangle r) {
        double pts[] = midPoints(r, 5);

        double min = Double.MAX_VALUE;
        int minPt = 0;
        for(int i = 0; i < pts.length; i += 2) {
            double distSq = Math.pow(pts[i] - pos.getX(), 2) + Math.pow(pts[i + 1] - pos.getY(), 2);
            if(distSq < min) {
                min = distSq;
                minPt = i;
            }
        }

        return new Point((int)pts[minPt], (int)pts[minPt + 1]);
    }

    public static double[] midPoints(Rectangle r, int numPoints) {
        double pts[] = new double[numPoints * 2 * 4];
        double segments[] = new double[] {
            r.x, r.y, r.x + r.width, r.y,
            r.x, r.y + r.height, r.x + r.width, r.y + r.height,
            r.x, r.y, r.x, r.y + r.height,
            r.x + r.width, r.y, r.x + r.width, r.y + r.height,
        };
        int ptNum = 0;
        for(int s = 0; s < segments.length / 4; s++) {
            double x1 = segments[s * 4 + 0];
            double y1 = segments[s * 4 + 1];
            double x2 = segments[s * 4 + 2];
            double y2 = segments[s * 4 + 3];
            for(int i = 0; i < numPoints; i++) {
                pts[ptNum++] = (x2 - x1) / (double)(numPoints - 1) * (double)i + x1;
                pts[ptNum++] = (y2 - y1) / (double)(numPoints - 1) * (double)i + y1;
            }
        }
        return pts;
    }
}
