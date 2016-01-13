package game.state.internalState;

import game.utilities.*;

import java.awt.geom.Point2D;

/**
 * The ball for the pong game. Mostly just a container
 * 
 * @author Matthew
 */
public class InternalBall {
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
    public InternalBall() {
        this(InternalBall.DEFAULT_LOCATION, Constants.DEFAULT_BALL_RADIUS, 
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
    public InternalBall(Point2D location, int radius, double direction, int speed) {
    	this.radius = radius;
    	setLocation(location);
    	setSpeed(speed);
    	setDirection(direction);
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
    	//Two while loops for normalizing the direction between 0 <= direction <= 2*Math.PI
    	if(direction > 2*Math.PI){
    		direction -= 2*Math.PI * ((int)(direction/(2*Math.PI)));
    	} else if (direction < 0){
    		direction += 2*Math.PI * ((int)(-1 * direction/(2*Math.PI)) + 1);
    	}
    	
    	this.direction = direction;
    }
    
    public int getRadius() {
        return radius;
    }
    
    public void stop(){
    	speed = 0;
    }
}
