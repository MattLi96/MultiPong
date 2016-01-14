package game.utilities;

import game.state.internalState.*;

import java.awt.geom.Point2D;

/**
 * Utilities class for various polygon methods to help with the polygon.
 * 
 * @author Matthew
 *
 */
public class PolygonUtilities {
	/**
	 * Calculates the x coordinates of the vertices of the given polygon. Meant
	 * to be used in conjunction with {@code calculateY}
	 * 
	 * @param radius
	 *            the radius of the polygon
	 * @param numSides
	 *            the number of sides of the polygon
	 * @return xPoints an array of the x coordinates of the points of the
	 *         polygon.
	 */
	public static int[] calculateX(int radius, int numSides) {
		int[] xPoints = new int[numSides];
		double theta = (2 * Math.PI) / numSides;
		for (int i = 0; i < numSides; i++) {
			if (numSides == 4)
				xPoints[i] = (int) (Math.cos(Math.PI / 4 + (i * theta)) * radius);
			else if (numSides == 6)
				xPoints[i] = (int) (Math.cos(Math.PI / 3 + (i * theta)) * radius);
			else
				xPoints[i] = (int) (Math.cos(Math.PI / 3
						+ Math.pow(0.5, numSides / 2 - 3) * Math.PI / 12
						+ (i * theta)) * radius);
		}
		return xPoints;
	}

	/**
	 * Calculates the y coordinates of the vertices of the given polygon. Meant
	 * to be used in conjunction with {@code calculateX}
	 * 
	 * @param radius
	 *            the radius of the polygon
	 * @param numSides
	 *            the number of sides of the polygon
	 * @return yPoints an array of the y coordinates of the points of the
	 *         polygon.
	 */
	public static int[] calculateY(int radius, int numSides) {
		int[] yPoints = new int[numSides];
		double theta = (2 * Math.PI) / numSides;
		for (int i = 0; i < numSides; i++) {
			if (numSides == 4)
				yPoints[i] = (int) (Math.sin(Math.PI / 4 + (i * theta)) * radius);
			else if (numSides == 6)
				yPoints[i] = (int) (Math.sin(Math.PI / 3 + (i * theta)) * radius);
			else
				yPoints[i] = (int) (Math.sin(Math.PI / 3
						+ Math.pow(0.5, numSides / 2 - 3) * Math.PI / 12
						+ (i * theta)) * radius);
		}
		return yPoints;
	}

	/**
	 * @return find the closest player to the given point. Returns "" if there
	 *         are no players
	 */
	public static String closestPlayer(InternalPolygon poly, Point2D pt) {
		String ret = "";
		double minDist = -1;
		boolean minSet = false;

		for (InternalSide s : poly.getSides()) {
			if (s.isPlayer()) {
				double dist = s.ptLineDist(pt);

				if (minSet && minDist < dist)
					continue;

				minSet = true;
				minDist = dist;
				ret = s.getPlayer();
			}
		}

		return ret;
	}
}
