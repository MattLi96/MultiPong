/*
 * Polygon.java
 */
package serializable;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

import pong.Pong;

/**
 * The Polygon class represents the absolute positioning of the polygon,
 * in addition to the code for collision detection. SOMETHING ABOUT 
 *
 * @author 2009-2010 WHS
 * <a href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class Polygon extends java.awt.Polygon implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -743886217744386449L;
	private int[] xArray, yArray;
    private Side[] sides;
    private int wallCounter = 0;
    private java.awt.Polygon container; // smaller Polygon object for collisions. I know it shouldn't be public but it's for a quick test.
    public static final short RADIUS = 1000;
    public static final short CONTAINER_RADIUS = 1000; //It's possible to change radius of ball without changing Ball.DEFAULT_RADIUS.

    /**
     * Constructs a regular polygon in an absolute frame of reference.
     * Also constructs a hidden, smaller polygon used for collision detection.
     * @param numSides Number of sides in the polygon
     */
    public Polygon(int numSides) {
        super(calculateX(RADIUS, numSides), calculateY(RADIUS, numSides), numSides);
        container = new java.awt.Polygon(calculateX(CONTAINER_RADIUS, numSides), calculateY(CONTAINER_RADIUS, numSides), numSides);
        xArray = calculateX(RADIUS, numSides);
        yArray = calculateY(RADIUS, numSides);
        sides = createSides();
    }
    
    public Polygon(Polygon p){
    	super(p.xpoints, p.ypoints, p.npoints);
    	container = p.container;
    	xArray = p.xArray;
    	yArray = p.yArray;
    	sides = p.sides;
    }

    /**
     * Returns side at position.
     *
     * @param num position of side to return
     * @return side at given position
     */
    public Side getSide(int num) {
        return sides[num];
    }
    
    /**
     * return the full array of sides
     * 
     * @return sides field
     */
    public Side[] getSides(){
    	return sides;
    }
    
    public int getNumSides(){
    	return sides.length;
    }

    /**
     * Set side at position.
     *
     * @param num position of side
     * @param side side to set at position
     */
    public void setSide(int num, Side side) {
        sides[num] = side;
    }

    /**
     * Set new player at position.
     *
     * @param num position of new player
     * @param name name of new player
     */
    public void setPlayer(int num, String name) {
        setSide(num, new Player(name, sides[num]));
        sides[num].setName(name);
    }

    /**
     * Creates the sides of the polygon.
     * @return polyLines an array of the polygon's sides.
     */
    private Side[] createSides() {
        sides = new Side[super.npoints];
        for (int i = 0; i < sides.length - 1; i++) {
            sides[i] = new Side(xArray[i], yArray[i], xArray[i + 1], yArray[i + 1], Side.Status.VACANT, i);
        }
        sides[super.npoints - 1] = new Side(xArray[super.npoints - 1], yArray[super.npoints - 1], xArray[0], yArray[0], Side.Status.VACANT, sides.length - 1);
        return sides;
    }

    /**
     * Calculates the x coordinates of the vertices of the given polygon.
     * @param radius the radius of the polygon
     * @param numSides the number of sides of the polygon
     * @return xPoints an array of the x coordinates of the points of the polygon.
     */
    public static int[] calculateX(int radius, int numSides) {
        int[] xPoints = new int[numSides];
        double theta = (2 * Math.PI) / numSides;
        for (int i = 0; i < numSides; i++) {
        	if(numSides == 4)
        		xPoints[i] = (int) (Math.cos(Math.PI/4 + (i * theta)) * radius);
        	else if(numSides == 6)
        		xPoints[i] = (int) (Math.cos(Math.PI/3 + (i * theta)) * radius);
        	else
        		xPoints[i] = (int) (Math.cos(Math.PI/3 + Math.pow(0.5, numSides/2 - 3) * Math.PI/12 + (i * theta)) * radius);
        }
        return xPoints;
    }

    /**
     * Calculates the y coordinates of the vertices of the given polygon.
     * @param radius the radius of the polygon
     * @param numSides the number of sides of the polygon
     * @return yPoints an array of the y coordinates of the points of the polygon.
     */
    public static int[] calculateY(int radius, int numSides) {
        int[] yPoints = new int[numSides];
        double theta = (2 * Math.PI) / numSides;
        for (int i = 0; i < numSides; i++) {
        	if(numSides == 4)
        		yPoints[i] = (int) (Math.sin(Math.PI/4 + (i * theta)) * radius);
        	else if(numSides == 6)
        		yPoints[i] = (int) (Math.sin(Math.PI/3 + (i * theta)) * radius);
        	else
        		yPoints[i] = (int) (Math.sin(Math.PI/3 + Math.pow(0.5, numSides/2 - 3) * Math.PI/12 + (i * theta)) * radius);
        }
        return yPoints;
    }

    /**
     * If next ball position is not contained and trajectory intersects 
     * a side, update ball direction and position.
     * RED_FLAG: should this all be factored to be in Ball, or something?
     *  
     * @param ball ball to check for colisions
     */
    public void checkCollision(Ball ball) {
        double direction = ball.getDirection();
        Point2D nextLocation = ball.getNextLocation();
        // Check whether ball will next be outside of container.
        if (!contains(nextLocation)) {
            // Find wall that intersects trajectory, if any.
            Point2D location = ball.getLocation();
            Line2D trajectory = new Line2D.Double(location, nextLocation);
            for (Side side : sides) {
            	Line2D wall = null;
            	if(side instanceof Player){
            		wall = side.adjustedPaddleLocation();
            	} else {
            		wall = side.paddleLocation();
            	}
/*
                System.out.printf("Paddle: %s %s\n", wall.getP1(), wall.getP2());
*/
                if (wall.intersectsLine(trajectory)) {
                    ball.changeDirection(bounce(wall, direction));
/*
                    System.out.printf("%s %s %s %s %s\n", 
                        wall.getP1(), wall.getP2(),
                        trajectory.getP1(), trajectory.getP2(),
                        pointOfIntersection(wall, trajectory));
*/
                    ball.setLocation(pointOfIntersection(wall, trajectory));
                    break;
                }
            }
        }
    }

    /**
     * Returns true if location is inside container polygon, false otherwise.
     *
     * @param location location to check
     * @return true if location is inside container polygon, false otherwise
     */
    @Override
    public boolean contains(Point2D location) {
        return container.contains(location);
    }

    private double bounce(Line2D wall, double direction) {
    	int side = Pong.getClosestPlayer();
    	double newSpin = 0;
    	
    	if (getSide(Pong.getClosestSide()) instanceof Player)
    		newSpin = getSide((int) (side / 2)).getPaddle().getMoving() * Math.PI/8;
    	else //if (getSide(Pong.getClosestSide()).getNumSide() % 2 == 0)
    		wallCounter++;
    	if (wallCounter > 5) {
    		if (Math.random() > 0.5)
    			newSpin = Math.PI/12;
    		else 
    			newSpin = -Math.PI/12;
    		wallCounter = 0;
    	}
    	
        double rise = wall.getY2() - wall.getY1();
        double run = wall.getX2() - wall.getX1();
        double wallAngle = Math.atan(rise / run) + (newSpin);
        // Trigonometry tells us that:
        // newDirection = wallAngle - (180 - WallAngle - (180 - direction))
        //              = 2 * wallAngle - direction
        return (-2) * wallAngle - direction;// RED_FLAG: explain the negative
    }

    /**
     * Returns a string representation of this polygon.
     *
     * @return a string representation of this polygon
     */
    @Override
    public String toString() {
        String delimiter = "";
        StringBuilder result = new StringBuilder("[");
        // Append "(X, Y)" values, separated by delimiter.
        for (int i = 0; i < super.npoints; i++) {
            result.append(delimiter).append("(").append(xArray[i])
                .append(", ").append(yArray[i]).append(")");
            delimiter = ", ";
        }
        return result.append("]").toString();
    }

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
