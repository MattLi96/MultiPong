package game.state.internalState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class maps players to the movement option they would like. This is
 * threadsafe
 * 
 * @author Matthew
 */
public class InternalControlState {
	/**
	 * The various movement options for a paddle.
	 */
	public enum Movement {
		LEFT, RIGHT, NONE
	};

	private ConcurrentHashMap<String, Movement> playerMove;

	/**
	 * @param players
	 *            The names of the players in the game
	 */
	public InternalControlState(Set<String> players) {
		playerMove = new ConcurrentHashMap<String, InternalControlState.Movement>();
		for (String player : players) {
			playerMove.put(player, Movement.NONE);
		}
	}

	/**
	 * @return the movement state of the given player. If the player not in
	 *         ControlState then returns NONE
	 */
	public Movement getMove(String player) {
		Movement move = playerMove.get(player);
		return move == null ? Movement.NONE : move;
	}

	public void setLeft(String player) {
		playerMove.put(player, Movement.LEFT);
	}

	public void setRight(String player) {
		playerMove.put(player, Movement.RIGHT);
	}

	public void setNone(String player) {
		playerMove.put(player, Movement.NONE);
	}
}
