package bundle.response;

import game.state.State;

/**
 * A response to a get state request
 * 
 * @author Matthew
 */
public class StateResponse extends Response {
	/**
	 * An up to date version of the game state
	 */
	public final State state;

	public StateResponse(State state) {
		super(true);
		this.state = state;
	}

}
