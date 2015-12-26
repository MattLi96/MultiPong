package game.state;

import game.utilities.*;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * The ball for the pong game. Mostly just a container
 * 
 * @author Matthew
 */
public class Ball {
	/**
	 * Default ball location. This should be the center of the polygon
	 */
	public static final Point2D DEFAULT_LOCATION = new Point2D.Double(0, 0);
	
	
	private final int radius; //Radius should not change
	private volatile Point2D location;
	
    /** Direction of the Ball in radians with 0 being to the east. */
    private volatile double direction;
    
    /** Speed of the Ball in absolute units / time tick. */
    private volatile int speed;
    
    /**
     * Creates a Ball with default location, radius, and speed, but with
     * a randomly determined direction.
     */
    public Ball() {
        this(Ball.DEFAULT_LOCATION, Constants.DEFAULT_RADIUS, 
        		Math.random() * (2 * Math.PI), Constants.DEFAULT_MAX_SPEED);
    }
    
    /**
     * Creates a ball with the specified speed and radius, but with
     * default location and randomly determined direction.
     * 
     * @param location ball location
     * @param radius ball radius
     * @param direction ball direction
     * @param speed ball speed
     */
    public Ball(Point2D location, int radius, double direction, int speed) {
    	this.location = location;
    	this.radius = radius;
    	this.direction = direction;
    	this.speed = speed;
    }
    
    public Point2D getLocation(){
    	return location;
    }
    
    public void setLocation(Point2D location){
    	this.location = location;
    }
    
    public int getSpeed(){
    	return speed;
    }
    
    public void setSpeed(int speed){
    	this.speed = speed;
    }
    
    public double getDirection(){
    	return direction;
    }
    
    public void setDirection(double direction){
    	this.direction = direction;
    }
    
    public int getRadius() {
        return radius;
    }
    
    public void stop(){
    	speed = 0;
    }
}
