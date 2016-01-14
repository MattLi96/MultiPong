package bundle.request;

import game.utilities.Constants;

public class GameRequest implements Request {
	public static enum GameRequestType {
		START_DEFAULT, START_LIVES, END
	}

	public final GameRequestType type;

	/**
	 * Lives should really only be used in the case of START_LIVES type
	 */
	public final int lives;

	/**
	 * Do not use this constructor for the START_LIVES type
	 * 
	 * @param type
	 */
	public GameRequest(GameRequestType type) {
		this(type, Constants.DEFAULT_LIVES);
		assert (type != GameRequestType.START_LIVES);
	}

	public GameRequest(GameRequestType type, int lives) {
		this.type = type;
		this.lives = lives;
	}
}
