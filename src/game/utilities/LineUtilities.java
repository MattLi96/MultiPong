package game.utilities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineUtilities {
    /**
     * DESCRIPTION (see
     * <a href="http://en.wikipedia.org/wiki/Cramer's_rule">Cramer's_rule</a>).
     * Precondition: A point of intersection exists between segments l1 and l2.
     *
     * @param l1 first line segment
     * @param l2 second line segment
     */
	public static Point2D pointOfIntersection(Line2D l1, Line2D l2) {
        /*
         * Given (x1, y1) and (x2, y2), in standard form, (a)x + (b)y = c:
         *
         *      a = (y2 - y1)
         *      b = -(x2 - x1)
         *      c = (x1 * y2 - x2 * y1)
         *
         * So:
         *      (y2 - y1)x -(x2 - x1)y = (x1 * y2 - x2 * y1)
         *
         * Who said linear equations wouldn't come in handy?
         */

        // Extract parameters from lines.
        double x11 = l1.getX1();
        double y11 = l1.getY1();
        double x12 = l1.getX2();
        double y12 = l1.getY2();
        double x21 = l2.getX1();
        double y21 = l2.getY1();
        double x22 = l2.getX2();
        double y22 = l2.getY2();

        // Calculate coefficients for two equations.
        double a = y12 - y11;
        double b = -(x12 - x11);
        double e = x11 * y12 - x12 * y11;
        double c = y22 - y21;
        double d = -(x22 - x21);
        double f = x21 * y22 - x22 * y21;

        // Apply Cramer's Rule to solve for x and y.
        assert a * d - b * c != 0;  // determinant must be non-zero
        double x = (e * d - b * f) / (a * d - b * c);
        double y = (a * f - e * c) / (a * d - b * c);

        return new Point2D.Double(x, y);
    }
}
