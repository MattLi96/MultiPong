/*
 * Paddle.java
 */

package serializable;

import java.io.Serializable;

/**
 * Paddle is a line type obstacle that is moved left and right on a side by
 * the player to block the ball from going into the goal.
 *
 * @author 2009-2010 WHS
 * <a href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class Paddle implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8919975105092892784L;

	/**
	 * The default width of a paddle as percent of the entire side length. 
         * Has to be >= 0 and <=100.
	 */
	public static final double DEFAULT_WIDTH = 20.0; //should these be ints?
	
	/**
	 * The default location of the center of a paddle as a percent of the
         * entire side length starting at 0 from the left. Has to be >= 0 and <=100.
	 */
	public static final double DEFAULT_CENTER = 50.0;
	
	/**
	 * The default value of one "move" of the paddle in percent of the
         * side length. Has to be >= 0 and <=100.
	 */
	public static final double ONE_MOVE = 5.0;
	
	/**
	 * Width of the paddle.
	 */
	private double width;
	
	/**
	 * Center of the paddle.
	 */
	private double center;
	
	/**
	 * Constructs a paddle and initializes its width and center with
         * default values.
	 */
	
	public int moving;
	
	public Paddle() {
		this.width = DEFAULT_WIDTH;
		this.center = DEFAULT_CENTER;
		this.moving = 0;
	}

	/**
	 * Constructs a paddle with a certain width percent and a default center.
	 */
	public Paddle(double widthPercent) {
		this.width = widthPercent;
		this.center = DEFAULT_CENTER;
		this.moving = 0;
	}
	
	/**
     * Returns the width of the paddle as a percent of the side length.
     * @return width of the paddle
     */
	public double getWidth() {
		return width;
	}
	
	/**
     * Returns the location of the center of the paddle as a percent of the side length
     * @return center of the paddle
     */
	public double getCenter() {
		return center;
	}
	
	/**
	 * Method is used for client server stuff, for the rest please use move methods
	 * @param c is the new center
	 */
	public void setCenter(double c){
		center = c;
	}
	
	public int getMoving(){
		return moving;
	}
	
	public void changeMoving(int n){
		moving = n;
	}
	
	/**
	 * Moves the paddle the designated number of moves to the right.
     * @param moves number of default movements to move
     * @return true if the paddle moved; false if it could not.
	 */
	public boolean moveRight(int moves) {
		center -= moves * ONE_MOVE;
		if(center > 100 - (width / 2)){ center = 100 - (width / 2); return false; }
		return true;
		
	}

	/**
	 * Moves the paddle the designated number of moves to the left.
     * @param moves number of default movements to move
     * @return true if the paddle moved; false if it could not.
	 */
	public boolean moveLeft(int moves) {
		center += moves * ONE_MOVE;
		if(center < width / 2){ center = width / 2; return false; }
		return true;
	}

	/**
	 * Moves the paddle to the right by ONE_MOVE.
     * Returns whether or not the paddle was capable of moving and moved.
	 * @return true if the paddle moved; false if it could not.
	 */
	public boolean moveRight() {
		changeMoving(1);
		if(center + ONE_MOVE <= 100 - (width / 2))
		{
			center += ONE_MOVE; return true;	
		}
		changeMoving(0);
		return false;
	}
	
	/**
	 * Moves the paddle to the left by ONE_MOVE.
     * Returns whether or not the paddle was capable of moving and moved.
	 * @return true if the paddle moved; false if it could not.
	 */
	public boolean moveLeft() {
		changeMoving(-1);
		if(center - ONE_MOVE >= (width / 2))
		{
			center -= ONE_MOVE; return true;
		}
		changeMoving(0);
		return false;
	}

//////////////////////////////////////////DON'T WORRY ABOUT THIS STUFF///////////////////////////////////////////////////

//	public boolean move(boolean direction) {   //call this method by running a loop calling move until the human's finger is lifted off the key
//		if(center - ONE_MOVE >= (width / 2) && center + ONE_MOVE <= 100 - (width / 2)) { //will truncation be a problem if using ints?
//			if(direction){center += ONE_MOVE;return true;}
//			if(!direction){center -= ONE_MOVE;return true;}
//		}
//		return false;
//	}

//        public static void main(String[] args) {
//            Paddle p = new Paddle();
//            System.out.println(p.getCenter());
//            p.move(-5);
//            System.out.println(p.getCenter());
//            p.move(true);
//            System.out.println(p.getCenter());
//
//        }
	
}
