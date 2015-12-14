/*
 * Side.java
 */

package serializable;

import java.awt.geom.Line2D;
import java.awt.event.*;
import java.io.Serializable;

import utilities.Constants;

/**
 * A side of the polygon that may or may not be occupied by a paddle.
 *
 * @author 2009-2010 WHS
 * <a href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class Side extends Line2D.Double implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8038223638236127416L;

	public static enum Status {VACANT, OCCUPIED}

    private Status status;
    private int numSide;
    private Paddle paddle;
    private String name = "";

    /**
     * Creates a side that may or may not contain a paddle.
     * @param X1 the x coordinate of the first vertex
     * @param Y1 the y coordinate of the first vertex
     * @param X2 the x coordinate of the second vertex
     * @param Y2 the y coordinate of the second vertex
     * @param status whether the side is a bumper or a paddle
     * @param numSide the number this side is
     */
    public Side(double X1, double Y1, double X2, double Y2, Status status, int numSide) {
        super(X1, Y1, X2, Y2);
        this.status = status;
        this.numSide = numSide;
        if (status == Status.VACANT) paddle = new Paddle(100);
        else paddle = new Paddle(Paddle.DEFAULT_WIDTH);
    }
    
    /**
     * Creates an empty side with no length. Nominally useless.
     */
    public Side() {
        this(0, 0, 0, 0, Status.VACANT, 0);
    }

    public int getPosition() {
        return numSide;
    }

    /**
     * Returns this side's paddle.
     * @return the paddle
     */
    public Paddle getPaddle() {
        return paddle;
    }

    /**
     * Returns the Side's Paddle's location.
     * @return Paddle's location
     */
    public Line2D.Double paddleLocation() {
        //double xCenter = (this.getX1() + this.getX2()) * (paddle.getCenter()/100.0);
        //double yCenter = (this.getY1() + this.getY2()) * (paddle.getCenter()/100.0);
        
        double paddleX1 = this.getX1() + ((this.getX2() - this.getX1()) * ((paddle.getCenter() - (paddle.getWidth() / 2)) / 100.0));
        double paddleY1 = this.getY1() + ((this.getY2() - this.getY1()) * ((paddle.getCenter() - (paddle.getWidth() / 2)) / 100.0));
        double paddleX2 = this.getX1() + ((this.getX2() - this.getX1()) * ((paddle.getCenter() + (paddle.getWidth() / 2)) / 100.0));
        double paddleY2 = this.getY1() + ((this.getY2() - this.getY1()) * ((paddle.getCenter() + (paddle.getWidth() / 2)) / 100.0));

        return new Line2D.Double(paddleX1, paddleY1, paddleX2, paddleY2);
    }
    
    public Line2D.Double adjustedPaddleLocation() {
    	double width = paddle.getWidth() + Constants.LINE_ADJUSTMENT;
        double paddleX1 = this.getX1() + ((this.getX2() - this.getX1()) * ((paddle.getCenter() - (width / 2)) / 100.0));
        double paddleY1 = this.getY1() + ((this.getY2() - this.getY1()) * ((paddle.getCenter() - (width / 2)) / 100.0));
        double paddleX2 = this.getX1() + ((this.getX2() - this.getX1()) * ((paddle.getCenter() + (width / 2)) / 100.0));
        double paddleY2 = this.getY1() + ((this.getY2() - this.getY1()) * ((paddle.getCenter() + (width / 2)) / 100.0));

        return new Line2D.Double(paddleX1, paddleY1, paddleX2, paddleY2);
    	
    }
    
    /**
     *Changes the status, does nothing if status is to remain the same.
     *@param newStatus the status to be changed to
     */
     public void setSideStatus(Status newStatus){
        if (status != newStatus){
            status = newStatus;
            if (status == Status.VACANT) paddle = new Paddle(100);
            else if (status == Status.OCCUPIED) paddle = new Paddle();
            //else if used to allow for easier inclusion of third enum
        }
     }
     
     public void move(KeyEvent e) {
         if(e.getKeyCode() == 37) {
             paddle.moveLeft();
         }
     }

     /**
      * Sets the player's name.
      * @param name Name of player
      */
     public void setName(String name) {
         this.name = name;
     }

     /**
      * Returns the player's name
      * @return Player's name
      */
     public String getName() {
         return name;
     }
     
     public int getNumSide() {
    	 return numSide;
     }
     
     public Status getStatus() {
         return status;
     }
}