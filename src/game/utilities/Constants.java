package game.utilities;

/**
 * List of constants you may want to adjust.
 * 
 * Constants you probably would not want to change are left in the actual files.
 * 
 * @author Matthew
 *
 */
public class Constants {
	// Pong Constant
	public static final int DEFAULT_LIVES = 10;
	
	// Paddle constants
	/**
	 * The default width of a paddle as percent of the entire side length. Has
	 * to be >= 0 and <=100.
	 */
	public static final double DEFAULT_WIDTH = 20.0;

	/**
	 * The default location of the center of a paddle as a percent of the entire
	 * side length starting at 0 from the left. Has to be >= 0 and <=100.
	 */
	public static final double DEFAULT_CENTER = 50.0;

	/**
	 * The default value of one "move" of the paddle in percent of the side
	 * length. Has to be >= 0 and <=100.
	 */
	public static final double MOVE_LENGTH = 5.0;

	// Side Constants
	/**
	 * Adjust the paddle length slightly to avoid near misses. Units are
	 * percent.
	 */
	public static final double LINE_ADJUSTMENT = 4.0;

	// Ball Constants
	/**
	 * Default ball radius. Not currently used for collisions.
	 */
	public static final int DEFAULT_RADIUS = 20;

	/**
	 * Default ball speed.
	 */
	public static final int DEFAULT_MAX_SPEED = 80;
	
	/**
	 * measured in units/sec^2
	 */
	public static final double DEFAULT_ACCELERATION = 10;
}
