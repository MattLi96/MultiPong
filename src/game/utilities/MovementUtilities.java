package game.utilities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import game.state.*;

public class MovementUtilities {
	/**
	 * Returns location that the ball will move to next.
	 * 
	 * @param ball
	 *            the ball.
	 * @return next location of the ball
	 */
	public static Point2D getNextLocation(Ball ball) {
		double newX = Math.cos(ball.getDirection()) * ball.getSpeed()
				+ ball.getLocation().getX();
		double newY = -1.0 * Math.sin(ball.getDirection()) * ball.getSpeed()
				+ ball.getLocation().getY();
		return new Point2D.Double(newX, newY);
	}

	/**
	 * @param side the side the ball is bouncing off of
	 * @param direction current direction of the ball
	 * @return the new direction of the ball
	 */
	public static double bounceDirection(Side side, double direction) {
		// TODO add in spin and repeat bounce detection (so ball doesn't get trapped)

		double rise = side.getY2() - side.getY1();
		double run = side.getX2() - side.getX1();
		double wallAngle = Math.atan(rise / run);
		return (-2) * wallAngle - direction;
	}

	/**
	 * @param wall
	 *            A line representing the wall a ball in bouncing on
	 * @param trajectory
	 *            A line representing the trajectory of a ball
	 * @return the location of a ball after bouncing on the given wall
	 */
	public static Point2D bounceLocation(Line2D wall, Line2D trajectory) {
		return LineUtilities.pointOfIntersection(wall, trajectory);
	}

	/**
	 * Container class with information about a collision, used for
	 * {@code checkCollision}
	 */
	public static class CollisionInfo {
		private Side side;
		private Line2D trajectory;
		private Line2D wall;

		public CollisionInfo(Side s, Line2D t, Line2D w) {
			side = s;
			trajectory = t;
			wall = w;
		}

		public Side side() {
			return side;
		}

		public Line2D trajectory() {
			return trajectory;
		}

		public Line2D wall() {
			return wall;
		}
	}

	/**
	 * Checks if the ball will collide with the polygon.
	 * 
	 * @param poly
	 * @param ball
	 * @return null if there is no collision, otherwise information about the
	 *         collision
	 */
	public static CollisionInfo checkCollision(Polygon poly, Ball ball,
			Point2D nextLocation) {
		// Check whether ball will next be outside of container.
		if (!poly.contains(nextLocation)) {
			// Find wall that intersects trajectory, if any.
			Point2D location = ball.getLocation();
			Line2D trajectory = new Line2D.Double(location, nextLocation);
			for (Side side : poly.getSides()) {
				Line2D wall = side.isPlayer() ? side.adjustedPaddleLocation()
						: side.paddleLocation();

				if (wall.intersectsLine(trajectory)) {
					return new CollisionInfo(side, trajectory, wall);
				}
			}
		}

		return null;
	}
}
