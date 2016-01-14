package game.state.internalState;

import java.awt.geom.Line2D;
import game.utilities.Constants;

/**
 * A side of a polygon that may or may not have a paddle and player This is
 * thread safe and lockless.
 * 
 * @author Matthew
 */

public class InternalSide extends Line2D.Double {
	/**
	 * No warning!
	 */
	private static final long serialVersionUID = 1685719509678508975L;

	private final int sideNum; // The number of this side

	private volatile String player; // Name of the player of this side if
									// relevant.
	private volatile InternalPaddle paddle; // The paddle representing this side

	/**
	 * Creates a side that may or may not contain a paddle.
	 * 
	 * @param X1
	 *            the x coordinate of the first vertex
	 * @param Y1
	 *            the y coordinate of the first vertex
	 * @param X2
	 *            the x coordinate of the second vertex
	 * @param Y2
	 *            the y coordinate of the second vertex
	 * @param sideNum
	 *            the number this side is
	 * @param playerName
	 *            the name of the Player on this side. Empty string, "",
	 *            indicates this side does not have a player. Value of
	 *            playerName cannot be null.
	 */
	public InternalSide(double X1, double Y1, double X2, double Y2,
			int sideNum, String playerName) {
		super(X1, Y1, X2, Y2);
		this.sideNum = sideNum;
		player = playerName;
		paddle = playerName.equals("") ? new InternalPaddle(100)
				: new InternalPaddle(Constants.DEFAULT_WIDTH);
	}

	/**
	 * Helper method for the two paddle location methods to reduce code.
	 */
	private Line2D.Double paddleLocationHelper(double width) {
		double center = paddle.getCenter();
		double paddleX1 = this.getX1()
				+ ((this.getX2() - this.getX1()) * ((center - (width / 2)) / 100.0));
		double paddleY1 = this.getY1()
				+ ((this.getY2() - this.getY1()) * ((center - (width / 2)) / 100.0));
		double paddleX2 = this.getX1()
				+ ((this.getX2() - this.getX1()) * ((center + (width / 2)) / 100.0));
		double paddleY2 = this.getY1()
				+ ((this.getY2() - this.getY1()) * ((center + (width / 2)) / 100.0));

		return new Line2D.Double(paddleX1, paddleY1, paddleX2, paddleY2);
	}

	/**
	 * @return This side's paddle location (absolutely)
	 */
	public Line2D.Double paddleLocation() {
		return paddleLocationHelper(paddle.getWidth());
	}

	/**
	 * @return Slightly longer version of this side's paddle for collision
	 *         detection
	 */
	public Line2D.Double adjustedPaddleLocation() {
		return paddleLocationHelper(paddle.getWidth()
				+ Constants.LINE_ADJUSTMENT);
	}

	/**
	 * Turns this into a normal side and removes the player
	 */
	public void removePlayer() {
		paddle = new InternalPaddle(100);
		player = "";
	}

	/**
	 * Moves this side's paddle to the left
	 */
	public void moveLeft() {
		paddle.moveLeft();
	}

	/**
	 * Moves this side's paddle to the right
	 */
	public void moveRight() {
		paddle.moveRight();
	}

	/**
	 * @return the side number of this side
	 */
	public int getSideNum() {
		return sideNum;
	}

	/**
	 * @return the player of this side or empty string ("") if this is not a
	 *         player.
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * @return if this side is a player or not
	 */
	public boolean isPlayer() {
		return !"".equals(player);
	}
}
