package game.state.internalState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Threadsafe.
 * 
 * @author Matthew
 */
public class InternalScore {
	//Name to number of lives left
	private ConcurrentHashMap<String, Integer> lives;
	private volatile String winner;

	/**
	 * Defaults the starting lives to 1
	 * 
	 * @param players
	 *            The names of the players in the game
	 */
	public InternalScore(Set<String> players) {
		this(players, 1);
	}

	/**
	 * @param players
	 *            The names of the players in the game
	 * @param startLives
	 *            The number of starting players
	 */
	public InternalScore(Set<String> players, int startLives) {
		winner = "";
		lives = new ConcurrentHashMap<String, Integer>();
		for (String player : players) {
			lives.put(player, startLives);
		}
	}

	/**
	 * Decrease the given players lives by the given number Can not decrease
	 * lives below zero.
	 * 
	 * @param player
	 *            the name of the player with lives to decrease. Requires this
	 *            score contains player
	 * @param num
	 *            The number of lives to decrease. Requires num is positive
	 */
	public void decreaseLife(String player, int num) {
		assert (num > 0 && lives.contains(player));

		Integer currLife = lives.get(player);
		if (currLife == null)
			return;

		int endLife = Math.max(currLife - 1, 0);
		lives.put(player, endLife);
	}

	/**
	 * @return Map of lives
	 */
	public Map<String, Integer> getLives() {
		return Collections.unmodifiableMap(lives);
	}

	/**
	 * @param player
	 *            the name of a player
	 * @return the number of lives the player has. Returns 0 if this does not
	 *         contain the player
	 */
	public int getLives(String player) {
		Integer ret = lives.get(player);
		return ret == null ? 0 : ret;
	}

	/**
	 * @return true if there is a winner, else false;
	 */
	public boolean checkWinner() {
		if (!"".equals(winner))
			return true;

		String win = null;
		for (String player : lives.keySet()) {
			if (lives.get(player) > 0) {
				if (win == null) {
					win = player;
				} else {
					return false;
				}
			}
		}

		if (win != null) {
			winner = win;
			return true;
		}

		return false;
	}

	/**
	 * @return returns the name of the winner. If there is no winner returns
	 *         empty string
	 */
	public String getWinner() {
		if (checkWinner()) {
			return winner;
		}
		return "";
	}
}
