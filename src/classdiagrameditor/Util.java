package classdiagrameditor;

import java.awt.Polygon;
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
}
