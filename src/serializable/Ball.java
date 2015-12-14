/*
 * Ball.java
 *
 * This is clearly not quite so wrong.
 *
 */

package serializable;

import java.awt.geom.Point2D;
import java.io.Serializable;

import pong.Pong;

import communicator.Communicator;
import communicator.Server;


/**
 * DESCRIPTION
 * 
 * @author 2009-2010 WHS <a
 *         href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class Ball implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8616302313026116271L;

	/*
     * The location of the ball.
     */
    private Point2D location = new Point2D.Double(0, 0);

    /**
     * The radius of the ball.
     */
    private int radius;

    /**
     * Direction of the Ball in radians with 0 being to the east.
     */
    private double direction;

    /**
     * Speed of the Ball in absolute units / time tick.
     */
    private int speed;
    
    /**
     * Server for the ball to send info across
     */
    
    private double spin;
    /**
     * Spin for the ball
     */
    private Server server = null;
    
    /**
     * Default ball radius.
     */
    public static final int DEFAULT_RADIUS = 20;

    
    /**
     * Default ball location.
     */
    public static final Point2D DEFAULT_LOCATION = new Point2D.Double(0, 0);

    /**
     * Default ball speed.
     */
    public static final int DEFAULT_SPEED = 80;

    private boolean moving = false;

    /**
     * Creates a Ball with default location, radius, and speed, but with
     *.randomly determined direction.
     */
    public Ball() {
        this(DEFAULT_LOCATION, DEFAULT_RADIUS, Math.random() * (2 * Math.PI), DEFAULT_SPEED, null);
    }
    
    public Ball(Communicator comm){
    	this(DEFAULT_LOCATION, DEFAULT_RADIUS, Math.random() * (2 * Math.PI), DEFAULT_SPEED, comm);
    }

    /**
     * Creates a ball with the specified speed and radius, but with
     * default location and randomly determined direction.
     * 
     * @param speed speed of the ball
     * @param radius radius of the ball
     */
    public Ball(int speed, int radius) {
        this(DEFAULT_LOCATION, radius, (Math.PI/(int)(Pong.getPolygon().getNumSides() / 2)) * (Math.random() * (int)(Pong.getPolygon().getNumSides() / 2)) + Math.random() * (Math.PI/(int)(Pong.getPolygon().getNumSides())), speed, null);
    }
    
    public Ball(Ball b){
    	this(b.location, b.radius, b.direction, b.speed, null);
    }

    /**
     * Creates a ball with the specified speed and radius, but with
     * default location and randomly determined direction.
     * 
     * @param location ball location
     * @param radius ball radius
     * @param direction ball direction
     * @param speed ball speed
     * @param occupied side last hit
     */
    public Ball(Point2D location, int radius, double direction, int speed, Communicator comm) {
        if(comm instanceof Server){
        	server = (Server) comm;
        }
    	setLocation(location);
        setRadius(radius);
        setSpin(0);
        changeDirection(direction);
        changeSpeed(speed);
    }

    public void stop() {
        moving = false;
    }

    public void start() {
        moving = true;
    }

    public boolean getState() {
        return moving;
    }

    /**
     * Returns ball location.
     * 
     * @return ball location
     */
    public Point2D getLocation() {
        return location;
    }

    /**
     * Sets ball location.
     * 
     * @param location new ball location
     */
    public void setLocation(Point2D location) {
        this.location.setLocation(location);
        if(server != null){
        	server.sendBallLocation(location);
        }
    }
    
    /**
     * Returns location that the Ball will move to next.
     * 
     * @return next location
     */
    public Point2D getNextLocation() {
        double newX = Math.cos(direction) * speed + location.getX();
        double newY = -1.0 * Math.sin(direction) * speed + location.getY();
        return new Point2D.Double(newX, newY);
    }

    /*
     * Moves the ball.
     */
    public void move() {
        setLocation(getNextLocation());
    }

    /**
     * Returns ball radius.
     * 
     * @return ball radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets ball radius.
     * 
     * @param radius new ball radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Returns ball speed.
     * 
     * @return ball speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Changes ball speed.
     * RED_FLAG: should be named setSpeed
     * 
     * @param speed new ball speed
     */
    public void changeSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Returns ball direction.
     * 
     * @return ball direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Changes ball direction.
     * RED_FLAG: should be named setDirection
     * 
     * @param direction new ball direction in radians
     */
    public void changeDirection(double direction) {
        this.direction = direction;
    }

    /**
     * Gets player who last hit ball, or null if no player has yet hit ball.
     *
     * @returns player who last hit ball
     */
    
    public double getSpin(){
    	return spin;
    }
    
    public void setSpin(double d){
    	spin = d;
    
    }
    
    public String toString(){
    	return getLocation().toString() + " " + getRadius() + " " + getDirection();
    }
}