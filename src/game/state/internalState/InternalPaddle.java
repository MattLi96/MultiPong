package game.state.internalState;


import game.utilities.Constants;

/**
 * A paddle, mostly a helper class for a side. Threadsafe and lockless
 * @author Matthew
 */
public class InternalPaddle {
	//Paddle instance stuff
	/** Width and center of this paddle */
	private final double width;
	private volatile double center;
	
	/**
	 * Constructs a player with default width and center. Use for players
	 */
	public InternalPaddle(){
		this(Constants.DEFAULT_WIDTH);
	}
	
	/**
	 * Constructs a player with given widthPercent. Set to 100 for a side
	 * @param widthPercent The percent of the side this paddle takes up
	 */
	public InternalPaddle(double widthPercent) {
		this.width = widthPercent;
		this.center = Constants.DEFAULT_CENTER;
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
	 * Moves the paddle the designated number of moves to the right.
     * @return true if the paddle moved; false moved to the end
	 */
	public void moveRight() {
		double c = center;
		if((c +  Constants.MOVE_LENGTH) > 100 - (width / 2)){
			c = 100 - (width / 2); 
			return; 
		}
		center = c + Constants.MOVE_LENGTH;
	}

	/**
	 * Moves the paddle the designated number of moves to the left.
	 */
	public void moveLeft() {
		double c = center;
		if((c - Constants.MOVE_LENGTH) < width / 2){ 
			center = width / 2; 
			return; 
		}
		center = c - Constants.MOVE_LENGTH;
	}
	
	
}
