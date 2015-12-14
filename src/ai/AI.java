package ai;
/*
 * AI.java
 */

import serializable.Ball;
import serializable.Side;


/**
 * Runs the Non-human player paddle and runs as a side
 *
 * @author 2009-2010 WHS
 * <a href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class AI extends Side {
	private static final long serialVersionUID = 1L;
	/**
	 * The Ball object which is used for Coordinates
	 */
	/**
	 * Constructs a new AI object
	 * @param X1 The first X coordinate for the paddle
	 * @param Y1 The first Y cooridnate for the paddle
	 * @param X2 The second X coordinate for the paddle
	 * @param Y2 The second Y cooridnate for the paddle
	 * @param numSide The side number
	 * @param x The Ball used by move()
	 */
	public AI(double X1, double Y1, double X2, double Y2, int numSide, Ball x) {
		super(X1, Y1, X2, Y2, Side.Status.OCCUPIED, numSide);
	}
	
	public boolean ballInt$$() {
		return true; //STUB
	}
	
	public void move() {
		
	}
}